FROM node:14-alpine

WORKDIR /app

# Копируем все файлы в текущую директорию
COPY . .

# Устанавливаем зависимости
RUN npm install

# Команда для запуска приложения
CMD ["npm", "start"]