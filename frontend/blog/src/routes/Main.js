import React from "react";
import { useNavigate } from "react-router-dom";

import '../styles/Main.css';
function Main(){
    const navigate = useNavigate();
 
    const navigateLogin = () => {
      navigate("/login");
    };
    const navigateSignup = () => {
        navigate("/signup");
      };

    return(
        <>
            <div id="main">
            <img id="logo" alt = "logo" src="img/aupuh_logo.png" />
            <h3 id="aupuh">어푸</h3>
            </div>
            <div id="login" onClick={navigateSignup}>
                <button>회원가입</button>
            </div>
            <div id="login">
                <button onClick={navigateLogin}>로그인</button>
            </div>
        </>        
        
    )
}

export default Main;