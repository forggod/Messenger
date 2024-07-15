FROM node:21-alpine
LABEL authors="mrpri"
WORKDIR /app
COPY . .
RUN npm install
RUN npm run build
EXPOSE 3000
CMD ["npm", "start"]
