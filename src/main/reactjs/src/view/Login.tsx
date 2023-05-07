import React, { useState } from 'react';
import styled from 'styled-components';
import { Button, Card, Form, Input, Typography, Divider } from 'antd';
import { CloseOutlined, CheckCircleOutlined, CheckCircleFilled } from '@ant-design/icons';
import { useNavigate } from "react-router-dom";
const { Title: AntTitle } = Typography;

function Login() {
    let navigate = useNavigate();
    const [isChecked, setIsChecked] = useState(false);

    const handleClick = () => {
        setIsChecked(!isChecked);
    };

    const title = (
        <CardHeader>
             <Title>
                <AntTitle level={5}>로그인</AntTitle>
            </Title>
            <CloseOutlined onClick={() => navigate(-1)} style={{marginTop: "4px", color: "#787878"}}/>
        </CardHeader>
    );

    return (
        <StyledBackgroud>
            <StyledCard title={title}>
                <div style={{marginBottom: "10px"}}>잇츠 다이어리에서 친구와 일기를 공유하세요 :)</div>
                <StyledForm>
                    <StyledFormItem name="email" rules={[{ required: true, message: '아이디를 입력해주세요', },]}>
                        <Input placeholder="아이디" />
                    </StyledFormItem>

                    <StyledFormItem name="password" rules={[{ required: true, message: '비밀번호를 입력해주세요', },]}>
                        <Input.Password placeholder="비밀번호" />
                    </StyledFormItem>

                    <StyledTextContain color={colors.lightgrey} justifyContent="start" marginBottom="10px" onClick={handleClick}>
                        {isChecked ? <CheckCircleFilled /> : <CheckCircleOutlined />}
                        <StyledCheckText>로그인 상태 유지</StyledCheckText>
                    </StyledTextContain>


                    <StyledFormItem>
                        <Button type="primary" htmlType="submit" style={{ width: '100%' }}>
                            로그인
                        </Button>
                    </StyledFormItem>

                    <StyledTextContain color={colors.darkgrey} justifyContent="center" marginBottom="0px">
                        <Text>회원가입</Text>
                        &nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
                        <Text>아이디/비밀번호 찾기</Text>
                    </StyledTextContain>

                    <Divider>또는</Divider>
                    
                    <StyledButton>
                        <StyledImage src={'img/google-icon.png'}/>
                        <ButtonText>구글 계정으로 로그인</ButtonText>
                    </StyledButton>
                    <StyledButton>
                        <StyledImage src={'img/facebook-icon.png'}/>
                        <ButtonText>페이스북 계정으로 로그인</ButtonText>
                    </StyledButton>
                    <StyledButton>
                        <StyledImage src={'img/naver-icon.png'}/>
                        <ButtonText>네이버 계정으로 로그인</ButtonText>
                    </StyledButton>
                    <StyledButton>
                        <StyledImage src={'img/kakao-icon.png'}/>
                        <ButtonText>카카오톡 계정으로 로그인</ButtonText>
                    </StyledButton>
                </StyledForm>
            </StyledCard>
        </StyledBackgroud>
    );
}

export default Login;

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

