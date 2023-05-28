/* eslint-disable */
import React, {useState} from 'react';
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Main from './routes/Main';
import Contact from './routes/Contact';
import About from './routes/About';
import LoginForm from './routes/LoginForm';
import Nav from './component/Nav';
import './App.css';
import "./Fonts/Font.css";
import SignupForm from './routes/SignupForm';



function App() {

  return (
      <div className='App'>
        <Nav />
        <Routes>
          <Route path='/' element={<Main />}/>
          <Route path='/about' element={<About />}/>
          <Route path='/contact' element={<Contact />}/>
          <Route path='/login' element={<LoginForm />}/>
          <Route path='/signup' element={<SignupForm />}/>
        </Routes>
      </div>
  );
}

export default App;
