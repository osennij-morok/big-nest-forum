FROM node:20-alpine AS build
WORKDIR /opt/frontend
COPY . .
COPY ./.env ./.env
RUN npm install --silent
RUN npm run build

FROM nginx:1.23.4-alpine
ARG FRONTEND_PORT
EXPOSE $FRONTEND_PORT
COPY --from=build /opt/frontend/dist/ /opt/frontend/
COPY --from=build /opt/frontend/nginx.conf.template ./nginx.conf.template
RUN DOLLAR="$" envsubst < ./nginx.conf.template > ./nginx.conf \
    && mv ./nginx.conf /etc/nginx/nginx.conf
RUN ln -sf /dev/stdout /var/log/nginx/access.log && \
    ln -sf /dev/stderr /var/log/nginx/error.log
CMD [ "nginx", "-g", "daemon off;" ]
