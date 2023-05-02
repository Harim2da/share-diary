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
        console.log(value);
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

    return (

        <StyledBackgroud>
            <StyledCard title={title}>
                <Button type="primary" loading={loadings[0]} onClick={() => enterLoading(0)}>
                    중복 체크
                </Button>
                <Button disabled type="primary" loading={loadings[0]} onClick={() => enterLoading(0)}>
                    중복 체크
                </Button>
                <Form name="form_item_path" layout="vertical" onFinish={onFinish}>
                    <MyFormItemGroup prefix={['user']}>
                        <MyFormItemGroup prefix={['name']}>
                            <MyFormItem name="firstName" label="아이디">
                                <Input />
                                <Button type="primary" loading={loadings[0]} onClick={() => enterLoading(0)}>
                                    중복 체크
                                </Button>
                            </MyFormItem>
                            <MyFormItem name="lastName" label="이메일">
                                <Input />
                            </MyFormItem>
                        </MyFormItemGroup>
                        <MyFormItem name="age" label="비밀번호">
                            <Input />
                        </MyFormItem>
                        <MyFormItem name="age" label="비밀번호 재입력">
                            <Input />
                        </MyFormItem>
                        <MyFormItem name="firstName" label="닉네임">
                            <Input />
                        </MyFormItem>
                    </MyFormItemGroup>

                    <Button type="primary" htmlType="submit">
                        가입하기
                    </Button>
                </Form>
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

const StyledForm = styled(Form)`
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    width: 100%;
`;

const CardHeader = styled.div`
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
  width: 100%;
`;

const StyledFormItem = styled(Form.Item)`
    width: 100%;
    margin-bottom: 10px;
`;

interface Container {
    justifyContent: string;
    color: string;
    marginBottom: string;
};

const StyledTextContain = styled.div<Container>`
    display: flex;
    align-items: center;
    justify-content: ${(props) => props.justifyContent};
    color: ${(props) => props.color};
    cursor: pointer;
    width: 100%;
    margin-bottom: ${(props) => props.marginBottom};
    font-size : 12px;
`;

const StyledCheckText = styled.span`
    color: ${colors.darkgrey};
    margin-left: 5px;
`;

const StyledButton = styled(Button)`
  display: flex;
  align-items: center;
  width: 100%;
  margin-top: 10px;
`;

const ButtonText = styled.span`
  flex: 1;
  text-align: center;
`;

const Text = styled.span`
    display: flex;
    align-items: center;
    color: ${colors.darkgrey};
    cursor: pointer;
    justify-content: start;

    &:hover{
        color : ${colors.blue};
      }
`;

const StyledImage = styled.img`
    width: 20px;
`;

