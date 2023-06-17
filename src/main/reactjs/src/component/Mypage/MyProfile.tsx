import React, { useState } from 'react';
import styled from 'styled-components';
import { useNavigate } from "react-router-dom";
import { Card, Mentions, Button, Select } from 'antd';

function MyProfile() {
    let navigate = useNavigate();

    const [nickname, setNickname] = useState<string>("짱소희94");
    const [password, setPassword] = useState<string>("");
    const [confirmPassword, setConfirmPassword] = useState<string>("");
    const [isModified, setIsModified] = useState<boolean>(false);

    const handleNicknameChange = (value: string) => {
        setNickname(value);
        setIsModified(true);
    };

    const handlePasswordChange = (value: string) => {
        setPassword(value);
        setIsModified(true);
    };

    const handleConfirmPasswordChange = (value: string) => {
        setConfirmPassword(value);
        setIsModified(true);
    };

    return (
        <Container>
            <ImgBox>
                <ProfileImg src='img/profile.jpg' />
            </ImgBox>
            <ProfileText>짱소희94</ProfileText>
            <Card style={{ width: '90%', margin: '20px auto', borderColor: '#c0c0c0' }} title="기본 정보">
                {/* 수정/작성 시 수정버튼 활성화 */}
                <InfoText>이메일</InfoText>
                <MyMentions
                    placeholder="skditjsdud35@naver.com"
                    disabled

                />
                <InfoText>닉네임</InfoText>
                <MyMentions
                    defaultValue="짱소희94"
                    onChange={handleNicknameChange}
                />
                <InfoText>비밀번호</InfoText>
                <MyMentions
                    placeholder="************"
                    onChange={handlePasswordChange}
                />
                <InfoText>비밀번호 확인</InfoText>
                <MyMentions
                    placeholder="************"
                    onChange={handleConfirmPasswordChange}
                />
                <Button style={{ textAlign: "center" }} type="primary" disabled={!isModified}>수정</Button>
            </Card>
            <Card style={{ width: '90%', margin: '20px auto', marginBottom: '100px', borderColor: '#c0c0c0' }} title="추가 정보">
                {/* 수정/작성 시 수정버튼 활성화 */}
                <InfoText>
                    나의 랭킹
                    <LinkText color="blue" onClick={() => navigate("/ranking")}> 랭킹 점수 보러가기 →</LinkText>
                </InfoText>
                <MyMentions
                    defaultValue="123위"
                    readOnly
                />
                <InfoText>그동안 쓴 일기</InfoText>
                <MyMentions
                    defaultValue="3921개"
                    readOnly
                />
                <InfoText>메달 획득 갯수</InfoText>
                <MyMentions
                    defaultValue="🥇 12개  🥈 3개  🥉 23개"
                    readOnly
                />
                <InfoText>일기방 나가기
                    <LinkText>삭제</LinkText>
                </InfoText>
                <Select
                    defaultValue="공유일기방"
                    style={{
                        width: '100%',
                    }}
                    options={[
                        {
                            value: '클라이밍방',
                            label: '클라이밍방',
                        },
                        {
                            value: '공유일기방',
                            label: '공유일기방',
                        },
                        {
                            value: '고양이조아방',
                            label: '고양이조아방',
                        },
                    ]}
                />
            </Card>
        </Container >
    );
}

export default MyProfile;

const Container = styled.div`
    height: 100%;
    overflow: auto;
`

const ImgBox = styled.div`
    width: 100px;
    height: 100px; 
    border-radius: 70%;
    overflow: hidden;
    margin: 0 auto;
    border-style: solid;
    border-width: 1px;
    color: #c8c8c8;
`
const ProfileImg = styled.img`
    width: 100%;
    height: 100%;
    object-fit: cover;
`

const ProfileText = styled.div`
    font-size: 20px;
    text-align: center;
`

const InfoText = styled.div`
    font-size: 15px;
    margin-bottom: 5px;
`

const MyMentions = styled(Mentions)`
    margin-bottom: 20px;
`

const LinkText = styled.span`
    float: right; 
    color: ${props => props.color === "blue" ? "#155bfe" : "red"};
    cursor: pointer;
    &:hover {
        color: ${props => props.color === "blue" ? "#3392ff" : "#ff7373"};
    }

`


