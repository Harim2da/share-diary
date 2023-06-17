import React, { useState } from 'react';
import styled from 'styled-components';
import ComponentsWrapper from "../styles/ComponentsWrapper";
import { useRecoilValue } from "recoil";
import { Tabs } from 'antd';
import { useNavigate } from "react-router-dom";
import EditInfo from '../component/Mypage/EditInfo';
import MyProfile from '../component/Mypage/MyProfile';
import NotiList from '../component/Mypage/NotiList';
import { height } from '@mui/system';

function MyPage() {

  const items = [
    {
      key: '1',
      label: '내 프로필',
      children: <MyProfile />,
    },
    {
      key: '2',
      label: '정보 수정',
      children: <EditInfo />,
    },
    {
      key: '3',
      label: '알림 내역',
      children: <NotiList />,
    },
  ];

  return (
    <ComponentsWrapper>
      <Tabs centered tabBarGutter={500} defaultActiveKey="1" items={items} />
    </ComponentsWrapper>
  );
}

export default MyPage;
