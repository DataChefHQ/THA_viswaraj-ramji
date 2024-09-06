# Use the official Nginx image from Docker Hub
FROM nginx:latest

# Copy the custom Nginx configuration file to the container
COPY nginx.conf /etc/nginx/nginx.conf

# Create the directory structure for images within the container
RUN mkdir -p /usr/share/nginx/html/files/images

# Expose the default Nginx port
EXPOSE 80