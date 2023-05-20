import React, { useState } from 'react';
import styled from 'styled-components';
import { Button, Card, Form } from 'antd';
import { CloseOutlined } from '@ant-design/icons';
import { useNavigate } from "react-router-dom";
import EmailAuth from "../component/FindMyInfo/EmailAuth"


const tabListNoTitle = [
    {
        key: 'findId',
        tab: '아이디 찾기',
    },
    {
        key: 'setPw',
        tab: '비밀번호 재설정',
    }
];

function FindIdPw() {
    let navigate = useNavigate();
    const [activeTabKey, setActiveTabKey] = useState<string>('findId');

    //타이틀 탭 변환 함수
    const onTabChange = (key: string) => {
        setActiveTabKey(key);
    };


    const contentListNoTitle: Record<string, React.ReactNode> = {
        findId: <EmailAuth isFindId={true} btnText="아이디 찾기" />,
        setPw: <EmailAuth isFindId={false} btnText="비밀번호 재설정" />,
        //버튼텍스트 , 아이디 패스워드 여부
    };

    return (
        <StyledBackgroud>
            <StyledCard
                tabList={tabListNoTitle}
                activeTabKey={activeTabKey}
                tabBarExtraContent={<CloseOutlined onClick={() => navigate(-1)} style={{ marginTop: "4px", color: "#787878" }} />}
                onTabChange={onTabChange}
            >
                {contentListNoTitle[activeTabKey]}
            </StyledCard>
        </StyledBackgroud>
    );

}

export default FindIdPw;

//색상 차트
const colors = {
    lightgrey: "#ababab",
    darkgrey: "#7d7d7d",
    blue: "#4287f5",
    background: "#f5f6fa"
};

const StyledBackgroud = styled.div`
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100vw;
    height: 100vh;
    background-color: ${colors.background};
`;

const StyledCard = styled(Card)`
  width: 400px;
  box-shadow: 0 3px 5px rgba(0, 0, 0, 0.15);
  border-radius: 10px;
  @media screen and (max-width: 500px) {
    width: 90%;
}
`;
