import React, { useState } from 'react';
import { useNavigate } from "react-router-dom";
import '../styles/SignupForm.css';


function SignupForm() {
  let container = document.getElementById('container')


  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmpassword, setConfirmPassword] = useState('');
  const [name, setName] = useState('');
  const [adress, setAdress] = useState('');
  const [number, setNumber] = useState('');

  const handleEmailChange = (e) => {
    setEmail(e.target.value);
  };

  const handlePasswordChange = (e) => {
    setPassword(e.target.value);
  };
  const handleConfirmPassword = (e) => {
    setConfirmPassword(e.target.value);
  };

  const handleNameChange = (e) => {
    setName(e.target.value);
  };

  const handleNumberChange = (e) => {
    setNumber(e.target.value);
  };
  
  const handleAdressChange = (e) => {
    setAdress(e.target.value);
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    // 회원가입 요청 처리
    // 이 부분에서 실제로 서버와 통신하여 회원가입 처리를 수행합니다.
    // 예시로는 간단히 콘솔에 회원가입 정보를 출력하는 예시를 보여줍니다.

    console.log('Email:', email);
    console.log('Password:', password);
    console.log('Name:', name);

    // 회원가입 후에 필요한 처리를 수행합니다.
    // 예를 들어, 회원가입 성공 시에 다른 페이지로 이동하거나 상태를 업데이트하는 등의 작업을 수행할 수 있습니다.
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
        <h1>회원가입</h1>
      </div>

      <div className='container'>
        <div className="row">
          <div className="col align-items-center flex-col sign-up">
            <div className="form-wrapper align-items-center">
              <div className="form sign-up">
                <div className="input-group">
                  <i className='bx bxs-user'></i>
                  <input type="text" placeholder="Username" value={name} onChange={handleNameChange}/>
                </div>
                <div class="input-group">
                  <i class='bx bx-mail-send'></i>
                  <input type="email" placeholder="Email" value={email} onChange={handleEmailChange}/>
                </div>
                <div class="input-group">
                  <i class='bx bxs-lock-alt'></i>
                  <input type="password" placeholder="Password" value={password} onChange={handlePasswordChange}/>
                </div>
                <div class="input-group">
                  <i class='bx bxs-lock-alt'></i>
                  <input type="password" placeholder="Confirm password" value={confirmpassword} onChange={handleConfirmPassword}/>
                </div>
                <div class="input-group">
                  <i class='bx bxs-lock-alt'></i>
                  <input type="text" placeholder="Phone number" value={number} onChange={handleNumberChange}/>
                </div>
                <div class="input-group">
                  <i class='bx bxs-lock-alt'></i>
                  <input type="text" placeholder="adress" value={adress} onChange={handleAdressChange}/>
                </div>
                <button>
                  Sign up
                </button>
                <p>
                  <span>
                    이미 가입하셨나요?
                  </span>
                  <div id="login">
                    <button onClick={navigateLogin}>로그인</button>
                  </div>
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default SignupForm;