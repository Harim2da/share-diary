import React, { useState, useEffect } from 'react';
import ComponentsWrapper from "../styles/ComponentsWrapper";
import { Tabs } from 'antd';
import MyProfile from '../component/Mypage/MyProfile';
import NotiList from '../component/Mypage/NotiList';

function MyPage() {
  const [tabBarGutter, setTabBarGutter] = useState(200);

  useEffect(() => {
    const handleResize = () => {
      if (window.innerWidth < 768) {
        setTabBarGutter(100);
      } else {
        setTabBarGutter(250);
      }
    };
    window.addEventListener('resize', handleResize);
    handleResize();
    return () => {
      window.removeEventListener('resize', handleResize);
    };
  }, []);

  const items = [
    {
      key: '1',
      label: '내 프로필',
      children: <MyProfile />,
    },
    {
      key: '2',
      label: '알림 내역',
      children: <NotiList />,
    },
  ];

  return (
    <ComponentsWrapper>
      <Tabs centered tabBarGutter={tabBarGutter} defaultActiveKey="1" items={items} style={{ height: '100%', overflow: 'auto' }} />
    </ComponentsWrapper>
  );
}

export default MyPage;