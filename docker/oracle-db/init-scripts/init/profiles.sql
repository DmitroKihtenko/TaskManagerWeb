CREATE PROFILE &tm_profile LIMIT
    SESSIONS_PER_USER          50
    CPU_PER_SESSION            UNLIMITED
    CPU_PER_CALL               UNLIMITED
    CONNECT_TIME               UNLIMITED
    LOGICAL_READS_PER_SESSION  DEFAULT
    LOGICAL_READS_PER_CALL     DEFAULT
    PRIVATE_SGA                DEFAULT
    COMPOSITE_LIMIT            DEFAULT
    FAILED_LOGIN_ATTEMPTS      5
    PASSWORD_LIFE_TIME         UNLIMITED
    PASSWORD_REUSE_TIME        30
    PASSWORD_REUSE_MAX         5
    PASSWORD_LOCK_TIME         1
    PASSWORD_GRACE_TIME        UNLIMITED;
