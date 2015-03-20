FROM java:8

ADD target/universal/akka-persistence-jdbc-play.tgz /
RUN mv /akka-persistence-jdbc-play /appl
RUN mv /appl/bin/akka-persistence-jdbc-play /appl/bin/start

RUN rm -rf /appl/bin/*.bat
RUN chown 1000:1000 /appl/bin/start
RUN chmod +x /appl/bin/start

WORKDIR /appl/bin
CMD [ "./start" ]