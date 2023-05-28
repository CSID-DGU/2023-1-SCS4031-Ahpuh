import { Link } from 'react-router-dom';
import React from 'react';
import './Nav.css';
import { Routes } from 'react-router-dom';

function Nav(){
    return (
        <div>
            <div className='navbar'>
                <Link className='navbarMenu' to = {'/'}>Main</Link>
                <Link className='navbarMenu' to = {'/about'}>About</Link>
                <Link className='navbarMenu' to = {'/contact'}>Contact</Link>
                <Link className='navbarMenu' to = {'/login'}>Login</Link>
            </div>
        </div>
    )
}

export default Nav;