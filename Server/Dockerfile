FROM node:14

WORKDIR /usr/src/app

COPY package*.json ./

RUN npm install

COPY public ./public

COPY server.js ./server.js

CMD [ "node", "server.js" ]