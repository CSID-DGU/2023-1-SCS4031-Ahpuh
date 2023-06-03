import { Link } from 'react-router-dom';
import React from 'react';
import './Nav.css';
import { Routes } from 'react-router-dom';

function Nav(){
    return (
        <div>
            <div className='navbar'>
                <Link className='navbarMenu' to = {'/realswim'}>실시간 수영장</Link>
                <Link className='navbarMenu' to = {'/member'}>회원 관리</Link>
                <Link className='navbarMenu' to = {'/setting'}>관리자 설정</Link>
                <Link className='navbarMenu' to = {'/login'}>로그인</Link>
            </div>
        </div>
    )
}

export default Nav;