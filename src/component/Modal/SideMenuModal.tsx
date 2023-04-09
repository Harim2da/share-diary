import React, { useState } from 'react';
import { Modal, Button, Checkbox, Form, Input } from 'antd';
import { MinusCircleOutlined, PlusOutlined } from '@ant-design/icons';

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

  //작성 취소 시 form의 내용 초기화
  const handleCancel = () => {
    form.resetFields();
    props.closeModal();
  };

  return (
    <Modal centered
      title={props.isCreate ? "일기방 만들기" : "일기방 초대하기"}
      visible={props.visible}
      onCancel={handleCancel}
      footer={[
        <Button key="submit" type="primary" onClick={props.closeModal}>
          {props.isCreate ? "방만들기" : "초대하기"}
        </Button>
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
            <Form.Item
              label="일기방 이름"
              name="diaryName"
            >
              <Input placeholder="일기방 이름을 입력해주세요. (최대 00자)" />
            </Form.Item>

            <Form.Item
              label="일기방 소개"
              name="diaryIntro"
            >
              <Input placeholder="일기방 소개를 입력해주세요. (최대 00자)" />
            </Form.Item>
          </>
        ) : null}


        < Form.List
          name="diaryInvite"
        initialValue={['']}>
        {(fields, { add, remove }, { errors }) => (
          <>
            {fields.map((field, index) => (
              <Form.Item
                {...(index === 0 ? formItemLayout : formItemLayoutWithOutLabel)}
                label={index === 0 ? '초대 보내기' : ''}
                required={false}
                key={field.key}
              >
                <Form.Item
                  {...field}
                  validateTrigger={['onChange', 'onBlur']}
                  noStyle
                >
                  <Input placeholder="초대받을 사람의 이메일을 입력해주세요. (최대 10명)" style={{ width: fields.length > 1 ? '95%' : '100%' }} />
                </Form.Item>
                {fields.length > 1 ? (
                  <MinusCircleOutlined
                    style={{ paddingLeft: '5px' }}
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
                  style={{ width: '100%' }}
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
