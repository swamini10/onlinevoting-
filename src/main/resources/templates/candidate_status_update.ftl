<html>
<head>
    <style>
        body { font-family: Arial, sans-serif; background: #f9f9f9; color: #222; }
        .container {
            background: #fff;
            padding: 24px;
            border-radius: 8px;
            max-width: 500px;
            margin: 40px auto;
            box-shadow: 0 2px 8px rgba(0,0,0,0.08);
        }
        .header { font-size: 22px; font-weight: bold; margin-bottom: 16px; color: #2a7ae2; }
        .status-box {
            padding: 15px;
            border-radius: 5px;
            margin: 16px 0;
            font-weight: bold;
        }
        .status-approved {
            background: #d4edda;
            border: 1px solid #c3e6cb;
            color: #155724;
        }
        .status-rejected {
            background: #f8d7da;
            border: 1px solid #f5c6cb;
            color: #721c24;
        }
        .status-pending {
            background: #fff3cd;
            border: 1px solid #ffeaa7;
            color: #856404;
        }
        .candidate-details {
            background: #f8f9fa;
            padding: 15px;
            border-radius: 5px;
            margin: 16px 0;
        }
        .candidate-details h4 {
            margin: 0 0 10px 0;
            color: #495057;
        }
        .note-section {
            background: #e9ecef;
            padding: 12px;
            border-left: 4px solid #6c757d;
            margin: 16px 0;
        }
        .footer { margin-top: 32px; font-size: 13px; color: #888; }
        .login-button {
            background: #2a7ae2;
            color: white;
            padding: 12px 24px;
            border-radius: 5px;
            text-decoration: none;
            display: inline-block;
            margin: 16px 0;
            font-weight: bold;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">📋 Candidate Application Status Update</div>
        
        <p>Hello <b>${candidateFirstName}</b>,</p>
        
        <p>We are writing to inform you about an update to your candidate application status.</p>
        
        <div class="status-box <#if status == 'Approved'>status-approved<#elseif status == 'Rejected'>status-rejected<#else>status-pending</#if>">
            <#if status == 'Approved'>
                ✅ Your application has been <strong>APPROVED</strong>
            <#elseif status == 'Rejected'>
                ❌ Your application has been <strong>REJECTED</strong>
            <#else>
                ⏳ Your application status: <strong>${status?upper_case}</strong>
            </#if>
        </div>
        
        <div class="candidate-details">
            <h4>Application Details:</h4>
            <p><strong>Name:</strong> ${candidateFirstName} <#if candidateMiddleName??>${candidateMiddleName}</#if> <#if candidateLastName??>${candidateLastName}</#if></p>
            <p><strong>Email:</strong> ${candidateEmail}</p>
            <#if partyName??>
                <p><strong>Party:</strong> ${partyName}</p>
            </#if>
            <#if electionName??>
                <p><strong>Election:</strong> ${electionName}</p>
            </#if>
            <p><strong>Application Date:</strong> ${applicationDate}</p>
            <p><strong>Status Updated:</strong> ${statusUpdateDate}</p>
        </div>
        
        <#if noteForStatus?? && noteForStatus?trim != "">
            <div class="note-section">
                <strong>Additional Notes:</strong><br/>
                ${noteForStatus}
            </div>
        </#if>
        
        <#if status == 'Approved'>
            <p>Congratulations! You are now eligible to participate as a candidate in the election. You can login to your account to view more details and manage your campaign.</p>
            <a href="${loginUrl!'/login'}" class="login-button">Login to Your Account</a>
        <#elseif status == 'Rejected'>
            <p>We appreciate your interest in participating as a candidate. If you have any questions about this decision or would like to reapply in the future, please contact our support team.</p>
        <#else>
            <p>Your application is still being processed. We will notify you once there are further updates.</p>
        </#if>
        
        <p>If you have any questions or need assistance, please don't hesitate to contact our support team.</p>
        
        <div class="footer">
            Regards,<br/>
            Online Voting Team<br/>
            <small>This is an automated message. Please do not reply to this email.</small>
        </div>
    </div>
</body>
</html>
