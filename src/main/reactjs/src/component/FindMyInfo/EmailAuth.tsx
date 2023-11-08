import React, { useState, useEffect } from 'react';
import styled from 'styled-components';
import { Button, Form, Input, Modal } from 'antd';
import axios from "axios";

interface EmailAuthProps {
    isFindId: boolean;
    btnText: string;
}

const EmailAuth: React.FC<EmailAuthProps> = ({ isFindId, btnText }) => {
    const [activeTabKey, setActiveTabKey] = useState<string>('findId');
    const [count, setCount] = useState<number | null>(null);    // 시간을 담을 변수
    const [text, setText] = useState("");

    const onChange = (e: any) => {
        setText(e.target.value);
    };

    //아이디 찾기, 비밀번호 재설정 버튼 클릭
    const clickFindBtn = () => {
        if (isFindId) {
            axios({
                method: "GET",
                url: "/api/member/me/id",
                params: {
                    email: text,
                },
            }).then((res) => {
                alert(`회원님의 아이디는 ${res.data.loginId} 입니다`)
            })
                .catch(function (error) {
                    alert("회원을 찾을 수 없습니다.");
                });
        }
    };

    //인증번호 발송 알림 모달
    const sendCertNumber = () => {
        Modal.success({
            content: '인증번호를 발송했습니다',
            onOk: () => {
                startTimer();
            },
        });
    };

    //타이머 시작 함수
    const startTimer = () => {
        setCount(180);
    };

    //초 -> 분:초 변환 함수
    const formatTime = (seconds: number): string => {
        const min = Math.floor(seconds / 60);
        const sec = seconds % 60;
        return `${min} : ${sec}`;
    };


    useEffect(() => {
        if (count !== null && count > 0) {
            const id = setInterval(() => {
                setCount((count) => (count ? count - 1 : 0));
            }, 1000);

            return () => clearInterval(id);
        }
    }, [count]);

    return (
        <Form name="form_item_path" layout="vertical">
            {isFindId ?
                <Form.Item
                    name="email"
                    label="이메일"
                    rules={[
                        {
                            type: 'email',
                            message: '유효하지 않은 이메일 형식입니다',
                        },
                        {
                            required: true,
                            message: '이메일을 입력해주세요',
                        },
                    ]}
                >
                    <div style={{ display: "flex" }}>
                        <Input onChange={onChange} />
                    </div>
                </Form.Item>
                :
                <>
                    <Form.Item
                        name="id"
                        label="아이디"
                        rules={[
                            {
                                required: true,
                                message: '아이디를 입력해주세요',
                            }
                        ]}
                    >
                        <div style={{ display: "flex" }}>
                            <Input />
                        </div>
                    </Form.Item>
                    <Form.Item
                        name="email"
                        label="이메일"
                        rules={[
                            {
                                type: 'email',
                                message: '유효하지 않은 이메일 형식입니다',
                            },
                            {
                                required: true,
                                message: '이메일을 입력해주세요',
                            },
                        ]}
                    >
                        <div style={{ display: "flex" }}>
                            <Input style={{ marginRight: "5px" }} />
                            <Button style={{ width: "150px" }} onClick={sendCertNumber}>인증번호 발송</Button>
                        </div>
                    </Form.Item>
                    <Form.Item
                        name="number"
                        label="인증번호"
                        rules={[
                            {
                                required: true,
                                message: '인증번호를 입력해주세요',
                            },
                        ]}>
                        <div style={{ display: "flex" }}>
                            <Input
                                style={{ marginRight: "5px" }}
                                suffix={<Timer>{count !== null ? formatTime(count) : ""}</Timer>}
                            />
                            <Button style={{ width: "150px" }}>확인</Button>
                        </div>
                    </Form.Item>
                </>}

            <Button type="primary" htmlType="submit" onClick={clickFindBtn}>
                {btnText}
            </Button>
        </Form>
    )
}
export default EmailAuth;

const Timer = styled.span`
  font-size: 12px;
  margin-left: 8px;
`;