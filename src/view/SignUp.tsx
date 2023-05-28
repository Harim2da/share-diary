import React, { useState } from 'react';
import styled from 'styled-components';
import { Button, Card, Form, Input, Typography, Divider } from 'antd';
import { CloseOutlined, CheckCircleOutlined, CheckCircleFilled } from '@ant-design/icons';
import { useNavigate } from "react-router-dom";
import type { FormItemProps } from 'antd';
const { Title: AntTitle } = Typography;

const MyFormItemContext = React.createContext<(string | number)[]>([]);

interface MyFormItemGroupProps {
    prefix: string | number | (string | number)[];
    children: React.ReactNode;
}

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


function SignUp() {
    let navigate = useNavigate();

    const title = (
        <CardHeader>
            <Title>
                <AntTitle level={5}>회원가입</AntTitle>
            </Title>
            <CloseOutlined onClick={() => navigate(-1)} style={{ marginTop: "4px", color: "#787878" }} />
        </CardHeader>
    );

    const onFinish = (value: object) => {
        setIsSubmitted(true);
    };

    const [loadings, setLoadings] = useState<boolean[]>([]);

    const enterLoading = (index: number) => {
        setLoadings((prevLoadings) => {
            const newLoadings = [...prevLoadings];
            newLoadings[index] = true;
            return newLoadings;
        });

        setTimeout(() => {
            setLoadings((prevLoadings) => {
                const newLoadings = [...prevLoadings];
                newLoadings[index] = false;
                return newLoadings;
            });
        }, 2000);
    };

    const [isSubmitted, setIsSubmitted] = useState(false);

    return (

        <StyledBackgroud>
            <StyledCard title={title}>
                {isSubmitted ? (
                    <>
                        <SuccessMessage>회원가입이 완료되었습니다.</SuccessMessage>
                        <Button type="primary" style={{ width: '100%' }} onClick={() => navigate('/login')}>
                            메인으로 
                        </Button>
                    </>
                ) : (
                    <Form name="form_item_path" layout="vertical" onFinish={onFinish}>
                        <MyFormItemGroup prefix={['user']}>
                            <MyFormItemGroup prefix={['name', 'firstName']}>
                                <MyFormItem name="firstName" label="아이디">
                                    <FormItemWraper>
                                        <Input style={{ marginRight: '8px' }} />
                                        <Button disabled type="primary" loading={loadings[0]} onClick={() => enterLoading(0)}>
                                            중복 체크
                                        </Button>
                                    </FormItemWraper>
                                </MyFormItem>
                                <MyFormItem name="lastName" label="이메일">
                                    <FormItemWraper>
                                        <Input style={{ marginRight: '8px' }} />
                                        <Button type="primary" loading={loadings[1]} onClick={() => enterLoading(1)}>
                                            인증하기
                                        </Button>
                                    </FormItemWraper>
                                </MyFormItem>
                            </MyFormItemGroup>
                            <MyFormItem name="password" label="비밀번호">
                                <Input.Password />
                            </MyFormItem>
                            <MyFormItem name="rePassword" label="비밀번호 재입력">
                                <Input.Password />
                            </MyFormItem>
                            <MyFormItem name="firstName" label="닉네임">
                                <Input />
                            </MyFormItem>
                        </MyFormItemGroup>

                        <Button type="primary" htmlType="submit" style={{ width: '100%' }}>
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


