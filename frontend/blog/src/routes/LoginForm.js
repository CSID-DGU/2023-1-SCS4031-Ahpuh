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

    // 로그인 요청 처리
    // 이 부분에서 실제로 서버와 통신하여 로그인 처리를 수행합니다.
    // 예시로는 간단히 콘솔에 로그인 정보를 출력하는 예시를 보여줍니다.
    console.log('Email:', email);
    console.log('Password:', password);

    // 로그인 후에 필요한 처리를 수행합니다.
    // 예를 들어, 로그인 성공 시에 다른 페이지로 이동하거나 상태를 업데이트하는 등의 작업을 수행할 수 있습니다.
  };

  const navigate = useNavigate();
  const navigateLogin = () => {
    navigate("/login");
  };
  const navigateSignup = () => {
      navigate("/signup");
  };

  return (
    <div className='wrapper'>
      <div class="title">
        <h1>로그인</h1>
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
                <button>
                  Login
                </button>
                <p>
                  <span>
                    회원이 아니신가요?
                  </span>
                  <div id="login">
                    <button onClick={navigateSignup}>회원가입</button>
                  </div>
                </p>
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