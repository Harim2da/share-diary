import React, { useState } from 'react';
import styled from 'styled-components';
import { Card } from 'antd';

function MyProfile() {
    return (
        <Card style={{ width: '90%', margin: '0 auto' }}>
            <ImgBox>
                <ProfileImg src='img/profile.jpeg' />
            </ImgBox>
        </Card>
    );
}

export default MyProfile;

const ImgBox = styled.div`
    width: 100px;
    height: 100px; 
    border-radius: 70%;
    overflow: hidden;
`
const ProfileImg = styled.img`
    width: 100%;
    height: 100%;
    object-fit: cover;
`
