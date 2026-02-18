<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Vote Confirmation - Online Voting System</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f4f7fa;
            color: #333333;
            line-height: 1.6;
        }
        
        .email-container {
            max-width: 600px;
            margin: 20px auto;
            background-color: #ffffff;
            border-radius: 8px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            overflow: hidden;
        }
        
        .header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: #ffffff;
            padding: 30px 20px;
            text-align: center;
        }
        
        .header h1 {
            margin: 0;
            font-size: 28px;
            font-weight: 600;
        }
        
        .header p {
            margin: 8px 0 0 0;
            font-size: 16px;
            opacity: 0.9;
        }
        
        .content {
            padding: 40px 30px;
        }
        
        .success-icon {
            text-align: center;
            margin-bottom: 30px;
        }
        
        .success-icon div {
            display: inline-block;
            width: 80px;
            height: 80px;
            border-radius: 50%;
            background-color: #28a745;
            color: white;
            font-size: 40px;
            line-height: 80px;
            margin-bottom: 15px;
        }
        
        .greeting {
            font-size: 18px;
            margin-bottom: 25px;
            color: #2c3e50;
        }
        
        .vote-details {
            background-color: #f8f9fa;
            border-radius: 8px;
            padding: 25px;
            margin: 25px 0;
            border-left: 4px solid #667eea;
        }
        
        .vote-details h3 {
            margin-top: 0;
            color: #495057;
            font-size: 18px;
            margin-bottom: 15px;
        }
        
        .detail-row {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 10px 0;
            border-bottom: 1px solid #e9ecef;
        }
        
        .detail-row:last-child {
            border-bottom: none;
        }
        
        .detail-label {
            font-weight: 600;
            color: #6c757d;
            flex: 1;
        }
        
        .detail-value {
            color: #2c3e50;
            font-weight: 500;
            flex: 2;
            text-align: right;
        }
        
        .confirmation-text {
            background-color: #d1ecf1;
            color: #0c5460;
            padding: 20px;
            border-radius: 6px;
            margin: 25px 0;
            text-align: center;
            font-weight: 500;
        }
        
        .important-note {
            background-color: #fff3cd;
            color: #856404;
            padding: 20px;
            border-radius: 6px;
            margin: 25px 0;
            border-left: 4px solid #ffc107;
        }
        
        .important-note h4 {
            margin: 0 0 10px 0;
            color: #856404;
        }
        
        .footer {
            background-color: #f8f9fa;
            padding: 30px 20px;
            text-align: center;
            border-top: 1px solid #dee2e6;
        }
        
        .footer p {
            margin: 0;
            color: #6c757d;
            font-size: 14px;
        }
        
        .contact-info {
            margin-top: 20px;
            padding-top: 20px;
            border-top: 1px solid #dee2e6;
        }
        
        .contact-info h4 {
            margin: 0 0 10px 0;
            color: #495057;
            font-size: 16px;
        }
        
        .social-links {
            margin-top: 15px;
        }
        
        .social-links a {
            display: inline-block;
            margin: 0 10px;
            color: #667eea;
            text-decoration: none;
            font-weight: 500;
        }
        
        .social-links a:hover {
            text-decoration: underline;
        }
        
        @media (max-width: 600px) {
            .email-container {
                margin: 10px;
                border-radius: 4px;
            }
            
            .content {
                padding: 30px 20px;
            }
            
            .header {
                padding: 25px 15px;
            }
            
            .header h1 {
                font-size: 24px;
            }
            
            .vote-details {
                padding: 20px 15px;
            }
            
            .detail-row {
                flex-direction: column;
                align-items: flex-start;
                gap: 5px;
            }
            
            .detail-value {
                text-align: left;
            }
        }
    </style>
</head>
<body>
    <div class="email-container">
        <!-- Header -->
        <div class="header">
            <h1>Vote Confirmed!</h1>
            <p>Your vote has been successfully recorded</p>
        </div>
        
        <!-- Content -->
        <div class="content">
            <!-- Success Icon -->
            <div class="success-icon">
                <div>✓</div>
                <p><strong>Vote Successfully Submitted</strong></p>
            </div>
            
            <!-- Greeting -->
            <div class="greeting">
                Dear <strong>${voterName!'Voter'}</strong>,
            </div>
            
            <p>Thank you for participating in the democratic process! Your vote has been successfully recorded and will be counted towards the final election results.</p>
            
            <!-- Vote Details -->
            <div class="vote-details">
                <h3>📋 Vote Details</h3>
                <div class="detail-row">
                    <span class="detail-label">Election:</span>
                    <span class="detail-value">${electionName!'Election'}</span>
                </div>
                <div class="detail-row">
                    <span class="detail-label">Voter ID:</span>
                    <span class="detail-value">${voterId!'N/A'}</span>
                </div>
                <div class="detail-row">
                    <span class="detail-label">Voting Date:</span>
                    <span class="detail-value">${votingDate!'Not specified'}</span>
                </div>
                <#if electionEndTime??>
                <div class="detail-row">
                    <span class="detail-label">Election Ends:</span>
                    <span class="detail-value">${electionEndTime}</span>
                </div>
                </#if>
            </div>
            
            <!-- Confirmation -->
            <div class="confirmation-text">
                🗳️ Your vote is <strong>secure</strong>, <strong>anonymous</strong>, and <strong>final</strong>. 
                You cannot change your vote once submitted.
            </div>
            
            <!-- Important Information -->
            <div class="important-note">
                <h4>⚠️ Important Information:</h4>
                <ul style="margin: 10px 0; padding-left: 20px;">
                    <li>Keep this confirmation email for your records</li>
                    <li>Your vote has been encrypted and securely stored</li>
                    <li>Results will be published after the election period ends</li>
                    <li>If you have any concerns, contact the election administrator</li>
                </ul>
            </div>
            
            <p style="margin-top: 30px;">
                Thank you for your participation in making democracy work! 
                <#if resultsAvailableTime??>
                Election results will be available on <strong>${resultsAvailableTime}</strong>.
                </#if>
            </p>
            
            <div class="contact-info">
                <h4>Need Help?</h4>
                <p>If you have any questions or concerns about your vote, please contact our support team:</p>
                <p>
                    📧 Email: <a href="mailto:${supportEmail!'support@onlinevoting.com'}">${supportEmail!'support@onlinevoting.com'}</a><br>
                    📞 Phone: ${supportPhone!'1-800-VOTE-HELP'}<br>
                    🌐 Website: <a href="${websiteUrl!'#'}">${websiteUrl!'www.onlinevoting.com'}</a>
                </p>
            </div>
        </div>
        
        <!-- Footer -->
        <div class="footer">
            <p><strong>Online Voting System</strong></p>
            <p>Secure • Transparent • Democratic</p>
            
            <div class="social-links">
                <a href="#">Privacy Policy</a>
                <a href="#">Terms of Service</a>
                <a href="#">Contact Support</a>
            </div>
            
            <p style="margin-top: 20px; font-size: 12px; color: #adb5bd;">
                This is an automated message. Please do not reply to this email.<br>
                © ${.now?string('yyyy')} Online Voting System. All rights reserved.
            </p>
        </div>
    </div>
</body>
</html>