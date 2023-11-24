import React, { useState, useEffect } from 'react';
import styled from 'styled-components';
import { useNavigate } from "react-router-dom";
import { Card, Mentions, Button, Select } from 'antd';
import PwModal from '../../component/Modal/ChangePasswordModal'
import InfoModal from '../../component/Modal/ChangeInfoModal'
import axios from 'axios'
import { useRecoilState } from "recoil";
import { loginState } from "../../atom/loginState";
import useModal from '../../hooks/useModal';

function MyProfile() {
    let navigate = useNavigate();
    let accessToken = localStorage.getItem('login-token');
    const [isPwModalVisible, showPwModal, closePwModal] = useModal();
    const [isInfoModalVisible, showInfoModal, closeInfoModal] = useModal();
    const [isLoggedIn, setIsLoggedIn] = useRecoilState(loginState);
    const [nickname, setNickname] = useState<string>('');
    const [email, setEmail] = useState<string>('');
    const [id, setId] = useState<string>('');

    useEffect(() => {
        if (!isLoggedIn) {
            navigate('/');
        } else {
            axios({
                method: "GET",
                url: '/api/member/myPage',
                headers: { Authorization: accessToken }
            })
                .then(res => {
                    setEmail(res.data.email)
                    setNickname(res.data.nickName)
                    setId(res.data.loginId)
                });
        }
    }, [isLoggedIn, navigate, accessToken]);

    return (
        <>
            <PwModal visible={isPwModalVisible} closeModal={closePwModal} />
            <InfoModal
                visible={isInfoModalVisible}
                closeModal={closeInfoModal}
                email={email}
                nickName={nickname}
            />
            <Container>
                <ImgBox>
                    <ProfileImg src='img/profile_icon.png' />
                </ImgBox>
                <ProfileText>{id}</ProfileText>
                <Card style={{ width: '90%', margin: '20px auto', borderColor: '#c0c0c0' }} title="기본 정보">
                    <InfoText>이메일</InfoText>
                    <MyMentions
                        value={email}
                        readOnly
                    />
                    <InfoText>닉네임
                        <Button style={{ float: 'right', marginBottom: '10px' }} onClick={() => showInfoModal()}>수정</Button>
                    </InfoText>
                    <MyMentions
                        value={nickname}
                        onChange={(value) => { setNickname(value); }}
                    />
                    <InfoText>비밀번호
                        <LinkText color="blue" onClick={() => showPwModal()}> 비밀번호 변경 →</LinkText>
                    </InfoText>
                    <MyMentions
                        defaultValue="***********"
                        readOnly
                    />
                </Card>
                <Card style={{ width: '90%', margin: '20px auto', marginBottom: '100px', borderColor: '#c0c0c0' }} title="추가 정보">
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
        </>
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
    width: 100%;
`

const MentionBox = styled(Mentions)`
    margin-bottom: 20px;
    width: 100%;
`

const LinkText = styled.span`
    float: right; 
    color: ${props => props.color === "blue" ? "#155bfe" : "red"};
    cursor: pointer;
    &:hover {
        color: ${props => props.color === "blue" ? "#3392ff" : "#ff7373"};
    }

`


