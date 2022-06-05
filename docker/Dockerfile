FROM tomcat:8.5.60-jdk11-openjdk AS backend-webserver

RUN ["apt", "update"]
RUN ["apt", "install", "-y", "maven"]

COPY ./tomcat/src/server.xml /usr/local/tomcat/conf/

ARG TM_USER="task_manager"
ARG TM_PASSWORD="task_manager"
ARG DB_URL="jdbc://oracle:thin:localhost:1521"

ENV TM_USER="${TM_USER}"
ENV TM_PASSWORD="${TM_PASSWORD}"
ENV DB_URL="${DB_URL}"

FROM gvenzl/oracle-xe:11.2.0.2-full AS data-storage

ARG TM_USER="task_manager"
ARG TM_PASSWORD="task_manager"

ENV TM_USER="${TM_USER}"
ENV TM_PASSWORD="${TM_PASSWORD}"

COPY oracle-db/init-scripts /container-entrypoint-initdb.d/
USER root
RUN ["chmod", "a+wr", "-R", "/container-entrypoint-initdb.d"]
RUN ["chmod", "a+x", "/container-entrypoint-initdb.d/1-forward-vars.sh"]
USER oracle
