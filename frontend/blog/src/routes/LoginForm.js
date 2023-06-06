import React, { useState } from 'react';
import { useNavigate } from "react-router-dom";


function LoginForm() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const handleEmailChange = (e) => {
    setEmail(e.target.value);
  };

  const handlePasswordChange = (e) => {
    setPassword(e.target.value);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log('Email:', email);
    console.log('Password:', password);
  };

  const navigate = useNavigate();
  const navigateLogin = () => {
    navigate("/login");
  };
  const navigateSignup = () => {
    navigate("/signup");
  };
  const navigateMain = () => {
    navigate("/realswim");
};


  const signIn = () => {
    fetch('3.37.74.123/admin/sign-in', { 
      method: 'POST',
      body: JSON.stringify({
        "email": email,
        "pwd": password,
      }),
    })
      .then(response => {
        if (response.message === 'SUCCESS') {
          window.localStorage.setItem('token',response.access_token);
          navigateMain();
        } else {
          alert('아이디 또는 비밀번호가 일치하지 않습니다.');
          console.log('Email:', email);
          console.log('Password:', password);
        }
      });
  };

  return (
    <div className='wrapper'>
      <div class="title">
        <h1 style={{fontFamily:"JalnanOTF"}}>로그인</h1>
        <img id="logo" style={{marginTop: "0%"}} alt = "logo" src="img/aupuh_logo.png" />
      </div>
      <div className='container'>
        <div className="row">
          <div className="col align-items-center flex-col sign-up">
            <div className="form-wrapper align-items-center">
              <div className="form login">
                <div class="input-group">
                  <i class='bx bx-mail-send'></i>
                  <input type="email" placeholder="Email" value={email} onChange={handleEmailChange}/>
                </div>
                <div class="input-group">
                  <i class='bx bxs-lock-alt'></i>
                  <input type="password" placeholder="Password" value={password} onChange={handlePasswordChange}/>
                </div>
                <button onClick={signIn}>로그인 </button>
                <div>
                  <p>
                  <span>
                    회원이 아니신가요?
                  </span>
                  <div id="login">
                    <button onClick={navigateSignup}style={{padding :"5px"}}>회원가입</button>
                  </div>
                  </p>  
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    // <form onSubmit={handleSubmit}>
    //   <div>
    //     <label>Email:</label>
    //     <input type="email" value={email} onChange={handleEmailChange} />
    //   </div>
    //   <div>
    //     <label>Password:</label>
    //     <input type="password" value={password} onChange={handlePasswordChange} />
    //   </div>
    //   <button type="submit">Login</button>
    // </form>
  );
}

export default LoginForm;