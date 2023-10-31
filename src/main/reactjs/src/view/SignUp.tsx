import React, { useState } from 'react';
import styled from 'styled-components';
import { Button, Card, Form, Input, Typography } from 'antd';
import { CloseOutlined } from '@ant-design/icons';
import { useNavigate } from "react-router-dom";
import type { FormItemProps } from 'antd';
import axios from 'axios';


const { Title: AntTitle } = Typography;
const MyFormItemContext = React.createContext<(string | number)[]>([]);

interface MyFormItemGroupProps {
    prefix: string | number | (string | number)[];
    children: React.ReactNode;
}

//styled-components와 antd의 폼 컴포넌트를 활용하여 중첩된 폼 필드 그룹을 생성하기 위한 함수들
function toArr(str: string | number | (string | number)[]): (string | number)[] {
    return Array.isArray(str) ? str : [str];
}

const MyFormItemGroup = ({ prefix, children }: MyFormItemGroupProps) => {
    const prefixPath = React.useContext(MyFormItemContext);
    const concatPath = React.useMemo(() => [...prefixPath, ...toArr(prefix)], [prefixPath, prefix]);

    return <MyFormItemContext.Provider value={concatPath}>{children}</MyFormItemContext.Provider>;
};

const MyFormItem = ({ name, ...props }: FormItemProps) => {
    const prefixPath = React.useContext(MyFormItemContext);
    const concatName = name !== undefined ? [...prefixPath, ...toArr(name)] : undefined;

    return <Form.Item name={concatName} {...props} />;
};

// 아이디 체크 (영문자로 시작하는 영문자 또는 숫자 6~20자)
function isId(asValue: string) {
    var regExp = /^[a-z]+[a-z0-9]{5,19}$/g;
    return regExp.test(asValue); // 형식에 맞는 경우 true 리턴
}

// 이메일 체크
function isEmail(asValue: string) {
    var regExp = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
    return regExp.test(asValue);
}

// 비밀번호 체크 (8 ~ 16자 영문, 숫자, 특수문자 조합)
function isPassword(asValue: string) {
    var regExp = /^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\-_=+]).{8,16}$/;
    return regExp.test(asValue); // 형식에 맞는 경우 true 리턴
}

// 닉네임 체크 (최대 20자)
function isNickname(asValue: string) {
    var regExp = /^.{1,20}$/;
    return regExp.test(asValue); // 형식에 맞는 경우 true 리턴
}


function SignUp() {
    let navigate = useNavigate();
    const [loadings, setLoadings] = useState<boolean[]>([]);
    const [isDuplication, setIsDuplication] = useState(false);
    const [isDuplicationLoading, setIsDuplicationLoading] = useState(false);
    const [isSubmitted, setIsSubmitted] = useState(false);
    const [id, setId] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [rePassword, setRePassword] = useState('');
    const [nickName, setNickName] = useState('');

    //회원가입 req
    const handleSignUp = () => {
        if (!isDuplication) {
            alert('아이디 중복체크를 해주세요.'); return;
        }

        if (!id || !email || !password || !rePassword || !nickName) {
            alert('모든 필드를 입력해 주세요.'); return;
        }

        if (password !== rePassword) {
            alert('비밀번호를 정확히 입력해 주세요.'); return;
        }

        if (!isId(id) || !isEmail(email) || !isPassword(password) || !isNickname(nickName)) {
            alert('필드를 형식에 맞추어 입력해 주세요.'); return;
        }

        axios({
            method: "post",
            url: "/api/member/signUp",
            data: {
                loginId: id,
                email: email,
                password: password,
                nickName: nickName,
            },
        }).then((res) => {
            setIsSubmitted(true);
        }).catch(function (error) {
            alert(error.response.data.message);
        });
    };

    //회원가입 카드 타이틀 컴포넌트
    const title = (
        <CardHeader>
            <Title>
                <AntTitle level={5}>회원가입</AntTitle>
            </Title>
            <CloseOutlined onClick={() => navigate(-1)} style={{ marginTop: "4px", color: "#787878" }} />
        </CardHeader>
    );

    //아이디 중복 체크
    const chkDupId = () => {
        setIsDuplicationLoading(true);
        axios({
            method: "post",
            url: "/api/member/loginId/validation",
            data: {
                loginId: id
            },
        }).then((res) => {
            setIsDuplicationLoading(false);
            if (res.data.validationLoginId) {
                alert("이미 존재하는 아이디입니다.")
            } else {
                setIsDuplication(true);
                alert("사용 가능한 아이디입니다.")
            }
        }).catch(function (error) {
            console.log(error.toJSON());
        });
    };

    return (

        <StyledBackgroud>
            <StyledCard title={title}>
                {isSubmitted ? (
                    <>
                        <SuccessMessage>회원가입이 완료되었습니다.</SuccessMessage>
                        <Button type="primary" style={{ width: '100%' }} onClick={() => navigate('/userLogin')}>
                            로그인 화면으로
                        </Button>
                    </>
                ) : (
                    <Form name="form_item_path" layout="vertical">
                        <MyFormItemGroup prefix={['user']}>
                            <MyFormItem name="id" label="아이디" rules={[
                                {
                                    validator: (_, value) =>
                                        isId(value) ? Promise.resolve() : Promise.reject('영문으로 시작하는 영문 또는 숫자 6~20자를 입력해 주세요.'),
                                },]}>
                                <FormItemWraper>
                                    <Input style={{ marginRight: '8px' }} onChange={e => setId(e.target.value)} disabled={isDuplication} />
                                    <Button type="primary" loading={isDuplicationLoading} onClick={chkDupId} disabled={isDuplication}>
                                        중복 체크
                                    </Button>
                                </FormItemWraper>
                            </MyFormItem>
                            <MyFormItem name="email" label="이메일" rules={[
                                {
                                    validator: (_, value) =>
                                        isEmail(value) ? Promise.resolve() : Promise.reject('이메일 형식이 아닙니다.'),
                                },]}>
                                <FormItemWraper>
                                    <Input style={{ marginRight: '8px' }} onChange={e => setEmail(e.target.value)} />
                                    <Button type="primary" loading={false}>
                                        인증하기
                                    </Button>
                                </FormItemWraper>
                            </MyFormItem>
                            <MyFormItem name="password" label="비밀번호" rules={[
                                {
                                    validator: (_, value) =>
                                        isPassword(value) ? Promise.resolve() : Promise.reject('영문, 숫자, 특수문자 조합의 8 ~ 16자를 입력해 주세요.'),
                                },]}>
                                <Input.Password onChange={e => setPassword(e.target.value)} />
                            </MyFormItem>
                            <MyFormItem name="rePassword" label="비밀번호 재입력" rules={[
                                {
                                    validator: (_, value) =>
                                        value && value === password ? Promise.resolve() : Promise.reject('비밀번호를 정확히 입력해 주세요.'),
                                },
                            ]}>
                                <Input.Password onChange={e => setRePassword(e.target.value)} />
                            </MyFormItem>
                            <MyFormItem name="nickname" label="닉네임" rules={[
                                {
                                    validator: (_, value) =>
                                        isNickname(value) ? Promise.resolve() : Promise.reject('최대 20자의 닉네임을 입력해 주세요.'),
                                },]}>
                                <Input onChange={e => setNickName(e.target.value)} />
                            </MyFormItem>
                        </MyFormItemGroup>
                        <Button type="primary" htmlType="submit" style={{ width: '100%' }} onClick={handleSignUp}>
                            가입하기
                        </Button>
                    </Form>)}
            </StyledCard>
        </StyledBackgroud>
    );
}

export default SignUp;

//색상 차트
const colors = {
    lightgrey: "#ababab",
    darkgrey: "#7d7d7d",
    blue: "#4287f5",
    background: "#f5f6fa"
};

const Title = styled.div`
    display: flex;
    align-items: center;
    justify-content: center;
    flex-grow: 1;
    margin-left: 15px;
`;

const StyledBackgroud = styled.div`
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100vw;
    height: 100vh;
    background-color: ${colors.background};
`;

const StyledCard = styled(Card)`
  width: 400px;
  box-shadow: 0 3px 5px rgba(0, 0, 0, 0.15);
  border-radius: 10px;
  @media screen and (max-width: 500px) {
    width: 90%;
}
`;

const CardHeader = styled.div`
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
  width: 100%;
`;

const FormItemWraper = styled.div`
  display: flex;
`;

const SuccessMessage = styled.div`
    display: flex;
    align-items: center;
    justify-content: center;
    height: 200px;
    font-size: 20px;
    font-weight: bold;
`;


