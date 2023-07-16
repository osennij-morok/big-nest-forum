FROM node:20-alpine AS build
WORKDIR /opt/frontend
COPY . .
COPY ./.env ./.env
RUN npm install --silent
RUN npm install -g react-scripts --silent
RUN npm run build

FROM nginx:1.23.4-alpine
EXPOSE 3300
COPY ./nginx.conf /etc/nginx/nginx.conf
COPY --from=build /opt/frontend/dist/ /opt/frontend/
RUN ln -sf /dev/stdout /var/log/nginx/access.log && \
    ln -sf /dev/stderr /var/log/nginx/error.log
CMD [ "nginx", "-g", "daemon off;" ]
