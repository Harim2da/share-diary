import React, { useState } from 'react';
import styled from 'styled-components';
import { Button } from 'antd';


function SocialLogin() {

    const githubURL = `https://github.com/login/oauth/authorize?client_id=${process.env.REACT_APP_GITHUB_CLIENT_ID}`
    const googleURL = 'https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.profile&redirect_uri=http://localhost:3000/login/oauth2/callback&response_type=code&client_id=225651924488-oq3i2bo15p16qvnmad43hpabphts956u.apps.googleusercontent.com'
    const kakaoURL = `https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=c7893734cf9af631da8451f73fe618b8&redirect_uri=http://localhost:3000/login/oauth2/callback`
    const naverURL = ``


    const handleGithub = () => {
        window.location.href = githubURL
    }

    const handleGoogle = () => {
        window.location.href = googleURL
    }

    const handleNaver = () => {
        window.location.href = naverURL
    }

    const handleKakao = () => {
        window.location.href = kakaoURL
    }

    return (
        <>
            <StyledButton onClick={handleGoogle}>
                <StyledImage src={'img/google-icon.png'} />
                <ButtonText>구글 계정으로 로그인</ButtonText>
            </StyledButton>

            <StyledButton onClick={handleGithub}>
                <StyledImage src={'img/github-icon.png'} />
                <ButtonText>깃허브 계정으로 로그인</ButtonText>
            </StyledButton>
            <StyledButton onClick={handleNaver}>
                <StyledImage src={'img/naver-icon.png'} />
                <ButtonText>네이버 계정으로 로그인</ButtonText>
            </StyledButton>
            <StyledButton onClick={handleKakao}>
                <StyledImage src={'img/kakao-icon.png'} />
                <ButtonText>카카오톡 계정으로 로그인</ButtonText>
            </StyledButton>
        </>
    );
}

export default SocialLogin;

const StyledButton = styled(Button)`
  display: flex;
  align-items: center;
  width: 100%;
  margin-top: 10px;
`;

const StyledImage = styled.img`
  width: 20px;
`;

const ButtonText = styled.span`
  flex: 1;
  text-align: center;
`;
