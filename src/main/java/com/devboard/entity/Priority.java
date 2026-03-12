package com.devboard.entity;


/**
 * Priority Enum - يحدد مستويات أولوية المهام
 * LOW: أولوية منخفضة
 * MEDIUM: أولوية متوسطة
 * HIGH: أولوية عالية
 * CRITICAL: أولوية حرجة
 */
public enum Priority {
    LOW(1),
    MEDIUM(2),
    HIGH(3),
    CRITICAL(4);
    
    private final int level;
    
    Priority(int level) {
        this.level = level;
    }
    
    public int getLevel() {
        return level;
    }
}
