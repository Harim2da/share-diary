import React, { useState } from "react";
import { Modal, Button, Checkbox, Form, Input } from "antd";
import { MinusCircleOutlined, PlusOutlined } from "@ant-design/icons";
import { loginState } from "../../atom/loginState";
import { useRecoilState, useRecoilValue } from "recoil";
import axios from "axios";
import { useParams } from "react-router-dom";
import { diaryUpdateState } from "../../atom/recoil";

const formItemLayout = {
  labelCol: {
    xs: { span: 24 },
    sm: { span: 4 },
  },
  wrapperCol: {
    xs: { span: 24 },
    sm: { span: 20 },
  },
};

const formItemLayoutWithOutLabel = {
  wrapperCol: {
    xs: { span: 24, offset: 0 },
    sm: { span: 20, offset: 4 },
  },
};

interface IModalProps {
  closeModal: () => void;
  visible: boolean;
  isCreate: boolean;
}

function SideMenuModal(props: IModalProps) {
  const [fieldsLength, setFieldsLength] = useState(1);
  const [form] = Form.useForm();
  const isLoggedIn = useRecoilValue(loginState);
  const [diaryUpdate, setDiaryUpdate] = useRecoilState(diaryUpdateState);
  const { diaryRoom } = useParams();

  //작성 취소 시 form의 내용 초기화
  const handleCancel = () => {
    form.resetFields();
    props.closeModal();
  };

  const handleInvite = () => {
    if (form.getFieldValue("diaryInvite")[0] === "") {
      alert("이메일을 입력해 주세요");
      return;
    }

    axios
      .post("/api/v0/member-invite-histories", {
        diaryRoomId: Number(diaryRoom),
        emails: form.getFieldValue("diaryInvite"),
      })
      .then((res) => {
        const message =
          res.status === 200
            ? "일기방 초대 메일이 발송되었습니다."
            : res.data.message;
        alert(message);
      })
      .catch((error) => {
        alert(error.response.data.error);
        console.log(error, "일기방 초대하기");
      })
      .finally(() => handleCancel());
  };

  const CreateDiaryRoom = () => {
    axios
      .get("/api/member/diary-room/validation", {
        headers: {
          Authorization: localStorage.getItem("login-token"),
        },
      })
      .then((res) => {
        if (res.status === 200) {
          if (res.data) {
            axios
              .post(
                "/api/v0/diary-rooms",
                {
                  name: form.getFieldValue("diaryName"),
                  emails: form.getFieldValue("diaryInvite"),
                },
                {
                  headers: {
                    Authorization: localStorage.getItem("login-token"),
                  },
                }
              )
              .then((res) => {
                const message =
                  res.status === 200
                    ? "일기방이 생성되었습니다"
                    : res.data.message;
                setDiaryUpdate(true);
                alert(message);
              })
              .catch((error) => {
                alert(error.response.data.error);
                console.log(error, "일기방 생성하기");
              })
              .finally(() => {
                setDiaryUpdate(false);
                handleCancel();
              });
          } else {
            alert("일기방은 최대 3개까지 생성 가능합니다.");
          }
        }
      })
      .catch((error) => console.log(error, "CreateDiaryRoom"));
  };

  const onClickBtn = () => {
    if (!isLoggedIn) {
      alert("로그인이 필요한 서비스입니다");
      handleCancel();
      return;
    }

    if (props.isCreate) {
      // 일기방 만들기
      CreateDiaryRoom();
    } else {
      // 일기방 초대하기
      handleInvite();
    }
  };

  return (
    <Modal
      centered
      title={props.isCreate ? "일기방 만들기" : "일기방 초대하기"}
      open={props.visible}
      onCancel={handleCancel}
      footer={[
        <Button
          key="submit"
          type="primary"
          onClick={onClickBtn}
          loading={diaryUpdate}
          disabled={diaryUpdate}
        >
          {props.isCreate ? "방만들기" : "초대하기"}
        </Button>,
      ]}
    >
      <Form
        name="basic"
        form={form}
        style={{ maxWidth: 600 }}
        initialValues={{ remember: true }}
        autoComplete="off"
      >
        {props.isCreate ? (
          <>
            <Form.Item label="일기방 이름" name="diaryName">
              <Input placeholder="일기방 이름을 입력해주세요. (최대 00자)" />
            </Form.Item>

            <Form.Item label="일기방 소개" name="diaryIntro">
              <Input placeholder="일기방 소개를 입력해주세요. (최대 00자)" />
            </Form.Item>
          </>
        ) : null}

        <Form.List name="diaryInvite" initialValue={[""]}>
          {(fields, { add, remove }, { errors }) => (
            <>
              {fields.map((field, index) => (
                <Form.Item
                  {...(index === 0
                    ? formItemLayout
                    : formItemLayoutWithOutLabel)}
                  label={index === 0 ? "초대 보내기" : ""}
                  required={false}
                  key={field.key}
                >
                  <Form.Item
                    {...field}
                    validateTrigger={["onChange", "onBlur"]}
                    noStyle
                  >
                    <Input
                      placeholder="초대받을 사람의 이메일을 입력해주세요. (최대 10명)"
                      style={{ width: fields.length > 1 ? "95%" : "100%" }}
                    />
                  </Form.Item>
                  {fields.length > 1 ? (
                    <MinusCircleOutlined
                      style={{ paddingLeft: "5px" }}
                      className="dynamic-delete-button"
                      onClick={() => remove(field.name)}
                    />
                  ) : null}
                </Form.Item>
              ))}
              <Form.Item>
                {fields.length < 10 ? (
                  <Button
                    type="dashed"
                    onClick={() => add()}
                    style={{ width: "100%" }}
                    icon={<PlusOutlined />}
                  >
                    추가하기
                  </Button>
                ) : null}

                <Form.ErrorList errors={errors} />
              </Form.Item>
            </>
          )}
        </Form.List>
      </Form>
    </Modal>
  );
}

export default SideMenuModal;
