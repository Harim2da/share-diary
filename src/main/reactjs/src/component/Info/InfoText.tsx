import styled from "styled-components";

function InfoText() {
    return (
        <InfoTextBackgroud>
            <InfoTitle>친구들과<br />속마음을 공유해보세요</InfoTitle>
            <InfoBox>
                <InfoCard>
                    <InfoBoxImage src='img/info-test1.jpeg' />
                    <InfoBoxText>일상 기록</InfoBoxText>
                </InfoCard>
                <InfoCard>
                    <InfoBoxImage src='img/info-test2.jpeg' />
                    <InfoBoxText>운동 기록</InfoBoxText>
                </InfoCard>
                <InfoCard>
                    <InfoBoxImage src='img/info-test1.jpeg' />
                    <InfoBoxText>반려동물 기록</InfoBoxText>
                </InfoCard>
            </InfoBox>
            <InfoTitle>.<br />.<br />.<br /></InfoTitle>
            <InfoTitle>다양한 삶의 기록을<br />남겨요</InfoTitle>
        </InfoTextBackgroud>
    );
}
export default InfoText;

const InfoTextBackgroud = styled.div`
    padding: 30px;
    margin: 80px;
`;

const InfoTitle = styled.div`
    padding: 30px;
    text-align: center;
    line-height : normal;
    font-size: 32px;
    font-weight: 600;
    font-family: Pretendard, -apple-system, BlinkMacSystemFont, "Helvetica Neue", "Apple SD Gothic Neo", "Spoqa Han Sans", 나눔고딕, NanumGothic, "Malgun Gothic", Arial, Helvetica, sans-serif;
`;

const InfoBox = styled.div`
    display: flex;
    justify-content: center; /* Center the children horizontally */
`;

const InfoCard = styled.div`
    padding: 30px;
    text-align: center;
    font-size: 25px;
    font-weight: 600;
    font-family: Pretendard, -apple-system, BlinkMacSystemFont, "Helvetica Neue", "Apple SD Gothic Neo", "Spoqa Han Sans", 나눔고딕, NanumGothic, "Malgun Gothic", Arial, Helvetica, sans-serif;
`;

const InfoBoxImage = styled.img`
    width: 300px;
    height: 300px;
    box-sizing: border-box;
    border-radius: 30px;

`;

const InfoBoxText = styled.div`
    padding: 20px;
`;