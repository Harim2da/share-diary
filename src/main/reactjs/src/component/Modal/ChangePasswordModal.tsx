import React from "react";
import { Modal, Button, Form, Input } from "antd";
import { loginState } from "../../atom/loginState";
import { useRecoilValue } from "recoil";
import axios from "axios";


interface IModalProps {
    closeModal: () => void;
    visible: boolean;
}

// 비밀번호 체크 (8 ~ 16자 영문, 숫자, 특수문자 조합)
function isPassword(asValue: string) {
    let regExp = /^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\-_=+]).{8,16}$/;
    // 형식에 맞는 경우 true 리턴
    return regExp.test(asValue);
}

function ChangePasswordModal(props: IModalProps) {
    const [form] = Form.useForm();
    let accessToken = localStorage.getItem('login-token');

    //작성 취소 시 form의 내용 초기화
    const handleCancel = () => {
        form.resetFields();
        props.closeModal();
    };

    //비밀번호 확인
    const reqChkPassword = (password: string) => {
        const res = axios({
            method: "POST",
            url: "/api/member/me/pwd",
            headers: {
                Authorization: accessToken,
            },
            data: {
                password: password,
            },
        })
        return res;
    };

    //비밀번호 변경
    const reqChangePassword = (password: string, updatePassword: string) => {
        axios({
            method: "POST",
            url: "/api/member/pwd",
            headers: {
                Authorization: accessToken,
            },
            data: {
                password: password,
                updatePassword: updatePassword
            },
        }).then((res) => {
            alert("비밀번호 변경을 완료했습니다.");
            handleCancel();
        })
    }

    const onFinish = (values: any) => {
        //새 비밀번호랑 새 비밀번호 확인이 다른 경우 
        if (values.cngPwd !== values.chkCngPwd) {
            alert('새 비밀번호가 일치하지 않습니다.'); return;
        }
        // 새 비밀번호가 형식에 맞지 않을 경우
        if (!isPassword(values.cngPwd)) {
            alert('필드를 형식에 맞추어 입력해 주세요.'); return;
        }
        // 비밀번호 확인 & 비밀번호 변경 req
        reqChkPassword(values.chkPwd).then((res) => {
            if (res.status === 200) {
                reqChangePassword(values.chkPwd, values.cngPwd)
            } else {
                alert('현재 비밀번호가 올바르지 않습니다.'); return;
            }
        })
    };

    type FieldType = {
        chkPwd?: string;
        cngPwd?: string;
        chkCngPwd?: string;
    };

    return (
        <Modal
            centered
            title="비밀번호 변경"
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
                <Form.Item<FieldType> label="현재 비밀번호" name="chkPwd">
                    <Input.Password placeholder="현재 비밀번호를 입력해주세요." />
                </Form.Item>
                <Form.Item<FieldType> label="새 비밀번호" name="cngPwd" rules={[
                    {
                        validator: (_, value) =>
                            isPassword(value) ? Promise.resolve() : Promise.reject('영문, 숫자, 특수문자 조합의 8 ~ 16자를 입력해 주세요.'),
                    },]}>
                    <Input.Password placeholder="새 비밀번호를 입력해주세요." />
                </Form.Item>
                <Form.Item<FieldType> label="새 비밀번호 확인" name="chkCngPwd" rules={[
                    {
                        validator: (_, value) =>
                            isPassword(value) ? Promise.resolve() : Promise.reject('영문, 숫자, 특수문자 조합의 8 ~ 16자를 입력해 주세요.'),
                    },]}>
                    <Input.Password placeholder="새 비밀번호를 다시 입력해주세요." />
                </Form.Item>
                <Form.Item>
                    <Button type="primary" htmlType="submit" style={{ float: 'right' }}>
                        비밀번호 변경
                    </Button>
                </Form.Item>
            </Form>
        </Modal>
    );
}

export default ChangePasswordModal;
