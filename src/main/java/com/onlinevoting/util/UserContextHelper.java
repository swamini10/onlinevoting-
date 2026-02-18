package com.onlinevoting.util;

import org.springframework.stereotype.Component;

@Component
public class UserContextHelper {
    
    private static final ThreadLocal<String> userEmailContext = new ThreadLocal<>();
    
    /**
     * Set the current user's email ID
     * @param emailId The email ID of the current user
     */
    public void setCurrentUserEmail(String emailId) {
        userEmailContext.set(emailId);
    }
    
    /**
     * Get the current user's email ID
     * @return The email ID of the current user
     */
    public String getCurrentUserEmail() {
        return userEmailContext.get();
    }
    
    /**
     * Clear the current user context
     */
    public void clearUserContext() {
        userEmailContext.remove();
    }
    
    /**
     * Check if user context is set
     * @return true if user email is set in context
     */
    public boolean hasUserContext() {
        return userEmailContext.get() != null;
    }
}