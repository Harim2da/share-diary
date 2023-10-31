import styled from "styled-components";

function UsageText() {
    return (
        <InfoTextBackgroud>
            <InfoTitle>잇츠 다이어리,<br />이렇게 시작해요</InfoTitle>
            <InfoBoxImage src="img/flow.png" />
        </InfoTextBackgroud>
    );
}
export default UsageText;

const InfoTextBackgroud = styled.div`
    border-radius: 30px;
    margin: 80px;
    background-color: #f5f6f8;
    margin-bottom: 200px;
`;

const InfoTitle = styled.div`
    padding: 30px;
    text-align: center;
    line-height : normal;
    font-size: 32px;
    font-weight: 600;
    font-family: Pretendard, -apple-system, BlinkMacSystemFont, "Helvetica Neue", "Apple SD Gothic Neo", "Spoqa Han Sans", 나눔고딕, NanumGothic, "Malgun Gothic", Arial, Helvetica, sans-serif;
`;

const InfoBoxImage = styled.img`
    width: 90%;
    margin: auto;
    display: block;
`;