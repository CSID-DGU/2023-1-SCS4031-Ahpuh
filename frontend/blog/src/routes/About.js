import React from "react";
import { useState } from "react";

function About(){
    const [file, setFile] = useState({});
    const videoUpload = e =>{
        const videoType = e.target.file[0].type.includes('video');

        setFile({
            url : URL.createObjectURL(e.target.files[0]),
            video:videoType,
        });
    }
    return(
        <div>
            <input type="file" onChange={videoUpload}/>
            {file.video &&  <video src= {file.url} width="750" height={500} controls />}
        </div>
    )
}

export default About;