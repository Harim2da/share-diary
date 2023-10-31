import styled from "styled-components";
import Slider from "react-slick";
import 'slick-carousel/slick/slick.css';
import 'slick-carousel/slick/slick-theme.css';

function Carousel() {
    return (
        <div>
            <StyledSlider {...settings}>
                <div>
                    <CardImg src='img/carousel_test1.png' />
                </div>
                <div>
                    <CardImg src='img/carousel_test2.png' />
                </div>
                <div>
                    <CardImg src='img/carousel_test3.png' />
                </div>
            </StyledSlider>
        </div>
    );
}
export default Carousel;

const settings = {
    dots: true,
    infinite: true,
    autoplay: true,
    speed: 3,
    autoplaySpeed: 5000,
    slidesToShow: 1,
    slidesToScroll: 1,
    responsive: [
        {
            breakpoint: 360, // 600px이하가 되면 
            settings: {
                dots: false, // 점을 안보이게 하겠다.
            },
        },
    ],
};

// 슬라이드 CSS
const StyledSlider = styled(Slider)`
.slick-list {
    width: 100%;
    margin: 0 auto;
}

.slick-slide div {
    /* cursor: pointer; */
}

.slick-track {
    /* overflow-x: hidden; */
}

`;

const CardImg = styled.img`
  width: 100%;
`;
