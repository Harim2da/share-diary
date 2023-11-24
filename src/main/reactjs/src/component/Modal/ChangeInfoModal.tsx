import React from "react";
import { Modal, Button, Form, Input } from "antd";
import axios from "axios";


interface ModalProps {
    closeModal: () => void;
    visible: boolean;
    email: string;
    nickName: string;
}

// 비밀번호 체크 (8 ~ 16자 영문, 숫자, 특수문자 조합)
function isPassword(asValue: string) {
    let regExp = /^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\-_=+]).{8,16}$/;
    // 형식에 맞는 경우 true 리턴
    return regExp.test(asValue);
}

function ChangeInfoModal(props: ModalProps) {
    const [form] = Form.useForm();
    let accessToken = localStorage.getItem('login-token');

    //작성 취소 시 form의 내용 초기화
    const handleCancel = () => {
        form.resetFields();
        props.closeModal();
    };

    const onFinish = (values: any) => {
        //비밀번호랑 비밀번호 확인이 다른 경우 
        if (values.chkPwd !== values.reChkPwd) {
            alert('비밀번호가 일치하지 않습니다.'); return;
        }

        // 비밀번호가 형식에 맞지 않을 경우
        if (!isPassword(values.chkPwd)) {
            alert('필드를 형식에 맞추어 입력해 주세요.'); return;
        }

        axios({
            method: "PATCH",
            url: '/api/member/me',
            headers: { Authorization: accessToken },
            data: {
                email: props.email,
                nickName: props.nickName,
                password: values.chkPwd,
                validationPassword: values.reChkPwd
            }
        }).then(res => {
            alert('닉네임 수정을 완료했습니다.');
            props.closeModal();
        });
    };

    type FieldType = {
        chkPwd?: string;
        reChkPwd?: string;
    };

    return (
        <Modal
            centered
            title="비밀번호 확인"
            open={props.visible}
            onCancel={handleCancel}
            footer={null}
        >
            <Form
                name="basic"
                form={form}
                style={{ maxWidth: 600, paddingTop: 20 }}
                initialValues={{ remember: true }}
                autoComplete="off"
                onFinish={onFinish}
            >
                <Form.Item<FieldType> label="비밀번호" name="chkPwd">
                    <Input.Password placeholder="비밀번호를 입력해주세요." />
                </Form.Item>
                <Form.Item<FieldType> label="비밀번호 확인" name="reChkPwd">
                    <Input.Password placeholder="비밀번호를 한번 더 입력해주세요." />
                </Form.Item>

                <Form.Item>
                    <Button type="primary" htmlType="submit" style={{ float: 'right' }}>
                        정보 수정
                    </Button>
                </Form.Item>
            </Form>
        </Modal>
    );
}

export default ChangeInfoModal;
