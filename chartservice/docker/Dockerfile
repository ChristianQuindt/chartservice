FROM registry.local.itscope.com:5001/itscope/docker/ansible_java14:latest

ARG MICROSERVICE_VERSION

RUN mkdir /opt/itscope
RUN curl --location --request GET "http://nexus.local.itscope.com/service/rest/v1/search/assets/download?group=de.itscope.metric-provider-api&name=chart-micro-service&maven.baseVersion=${MICROSERVICE_VERSION}&sort=version&maven.extension=jar" \
    # Nexus admin credentials
    --header 'Authorization: Basic YWRtaW46YWRtaW4xMjM=' --output /opt/itscope/microservice.jar
COPY run /usr/bin/run
RUN chmod 755 /usr/bin/run

CMD ["/usr/bin/run"]