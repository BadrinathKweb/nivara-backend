import React from "react";
import "../styles/landing.css";
function LandingPage() {
  return (
    <div className="app-container">
      <div className="content-card">

        {/* Your Logo */}
        <img
          src="/Nivara_Logo.png"
          alt="Nivara Logo"
          className="logo"
        />

        {/* Title */}
        <h1 className="title">Welcome to Nivara</h1>
        <p className="subtitle">
          Smart • Shared • Sustainable Travel
        </p>

        {/* Action Buttons */}
        <div className="button-group">
          <button className="primary-btn">Login</button>
          <button className="secondary-btn">Sign Up</button>
        </div>

      </div>
    </div>
  );
}

export default LandingPage;
