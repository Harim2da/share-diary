import React, { useState } from 'react';
import styled from 'styled-components';
import { Card, Button } from 'antd';
import { useMediaQuery } from 'react-responsive'

function NotiList() {
    const isDesktopOrMobile = useMediaQuery({ query: '(max-width:768px)' });

    const [buttonText, setButtonText] = useState('수락');
    const [showNotification, setShowNotification] = useState(false);

    const handleButtonClick = (text: string) => {
        if (text === '수락') {
            setButtonText('수락');
        } else {
            setButtonText('거절');
        }
        setShowNotification(true);
    };
    return (
        <Container>
            <DateText>2023.08.23</DateText>
            <MyCard>
                {showNotification ? (
                    <NotiContainer>
                        <div> alka님이 보낸 프로젝트 스타트업팀빌딩 일기방 초대에 {buttonText}하셨습니다.</div>
                    </NotiContainer>
                ) : (
                    <NotiContainer>
                        <NotiText> alka님이 프로젝트 스타트업팀빌딩 일기방에 초대하였습니다.</NotiText>
                        <ButtonContainer>
                            <MyButton type="primary" onClick={() => handleButtonClick("수락")}>수락</MyButton>
                            <MyButton onClick={() => handleButtonClick("거절")}>거절</MyButton>
                        </ButtonContainer>
                    </NotiContainer>
                )}
            </MyCard>
            <MyCard>
                <NotiContainer>
                    <div> alka님이 보낸 프로젝트 스타트업팀빌딩 일기방 초대에 수락하셨습니다.</div>
                </NotiContainer>
            </MyCard>
            <MyCard>
                <NotiContainer>
                    <div> alka님이 보낸 프로젝트 스타트업팀빌딩 일기방 초대에 거절하셨습니다.</div>
                </NotiContainer>
            </MyCard>
            <DateText>2023.08.22</DateText>
            <MyCard>
                <NotiContainer>
                    <div> alka님이 보낸 프로젝트 스타트업팀빌딩 일기방 초대에 수락하셨습니다.</div>
                </NotiContainer>
            </MyCard>
            <MyCard style={{ marginBottom: '100px' }}>
                <NotiContainer>
                    <div> alka님이 보낸 프로젝트 스타트업팀빌딩 일기방 초대에 거절하셨습니다.</div>
                </NotiContainer>
            </MyCard>
        </Container>
    );
}

export default NotiList;

const Container = styled.div`
    height: 100%;
    overflow: auto;
`
const MyCard = styled(Card)`
    width: 90%;
    margin: 20px auto;
    border-color: #c0c0c0;
    display: flex;
    flex-direction: column;
    
`

const DateText = styled.div`
    font-size: 20px;
    width: 90%;
    margin: 20px auto;
`
const NotiContainer = styled.div`
    display: flex;
    justify-content: space-between;
`;

const ButtonContainer = styled.div`
`

const NotiText = styled.div`
    margin-top : 7px;
`

const MyButton = styled(Button)`
    margin : 3px;
    padding: 0 30px;
    float: right;
`
