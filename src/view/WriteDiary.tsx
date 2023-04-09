import { useState } from 'react'
import ComponentsWrapper from "../styles/ComponentsWrapper";
import { Card, Space, Button, Input, Divider } from 'antd';
import styled from 'styled-components';

const { TextArea } = Input;

function WriteDiary() {
    const [selectedDiary, setSelectedDiary] = useState<number[]>([]);
    const [selectedFace, setSelectedFace] = useState<number | null>(null);
    const [value, setValue] = useState('');

    const today: Date = new Date();
    const year: number = today.getFullYear();
    const month: number = today.getMonth() + 1;
    const date: number = today.getDate();

    const face = [
        'img/face-happy.png',
        'img/face-sceptic.png',
        'img/face-cool.png',
        'img/face-muted.png',
        'img/face-sad.png'
    ];

    const diary = [
        '클라이밍 일기방',
        '여행 일기방',
        '멍멍이 자랑 일기방',
        '집가고 싶은 사람들의 일기방',
        '클라이밍 일기방',
        '여행 일기방',
        '멍멍이 자랑 일기방',
        '집가고 싶은 사람들의 일기방',
    ];

    const handleDiaryClick = (index: number) => {
        if (selectedDiary.includes(index)) {
            setSelectedDiary(selectedDiary.filter((selectedIndex) => selectedIndex !== index));
        } else {
            setSelectedDiary([...selectedDiary, index]);
        }
    };

    const handleFaceClick = (index: number) => {
        setSelectedFace(index);
    };

    return <ComponentsWrapper>
        <Divider orientation="left">
            <div style={{ fontSize: '30px' }}>{year}년 {month}월 {date}일 오늘의 일기</div>
        </Divider>
        <TextArea
            style={{ margin: '0 auto', width: '85%', display: 'block', marginBottom: '20px', fontSize: '20px' }}
            value={value}
            onChange={(e) => setValue(e.target.value)}
            placeholder="오늘 하루를 글로 표현해주세요"
            autoSize={{ minRows: 8, maxRows: 15 }}
        />

        <Space direction="vertical" size={16} style={{ margin: '0 auto', width: '85%', display: 'block' }}>
            <Card title="오늘의 기분은 어떠셨나요?" style={{ marginBottom: '20px' }}>
                <ImageWrap>
                    {face.map((faceUrl, index) => (
                        <Image
                            key={index}
                            src={faceUrl}
                            style={selectedFace === index ? { backgroundColor: '#ffd400', borderRadius: '50%' } : {}}
                            onClick={() => handleFaceClick(index)}
                        />
                    ))}
                </ImageWrap>
            </Card>
            <Card title="어느 일기방에 올릴까요?" style={{ marginBottom: '20px' }}>
                <Space wrap>
                    {diary.map((buttonText, index) => (
                        <Button
                            key={index}
                            type={selectedDiary.includes(index) ? 'primary' : 'default'}
                            onClick={() => handleDiaryClick(index)}
                        >
                            {buttonText}
                        </Button>
                    ))}
                </Space>
            </Card>
        </Space>
        <Button type='primary' style={{ margin: '0 auto', width: '30%', display: 'block', marginBottom: '20px' }}>등록하기</Button>
    </ComponentsWrapper>;
}

export default WriteDiary;

const Image = styled.img`
    width: 50px;

    &:hover {
        background-color: #ffd400;
        border-radius: 50%;
      }
`;

const ImageWrap = styled.div`
    display: flex;
    justify-content: space-around;
`;
