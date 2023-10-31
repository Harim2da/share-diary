import React, { useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import axios from 'axios';
import { useRecoilState } from 'recoil';
import { loginState } from '../atom/loginState';
import { useNavigate } from "react-router-dom";

function Callback() {
    let navigate = useNavigate();
    const [isLoggedIn, setIsLoggedIn] = useRecoilState(loginState);
    const location = useLocation();
    const code = new URLSearchParams(location.search).get('code');
    const provider = "google"
    useEffect(() => {
        if (code) {
            axios({
                method: "POST",
                url: `/api/auth/social/${provider}`,
                data: {
                    code: code
                },
            }).then((res) => {
                console.log(res)
                localStorage.setItem('login-token', res.data.accessToken);
                setIsLoggedIn(true);
                navigate("/");
            }).catch(function (error) {
                console.log(error.toJSON());
            });
        }
    }, []);

    return (
        <div>
            <div>로그인 중입니다. </div>
        </div>
    );
}
export default Callback;
