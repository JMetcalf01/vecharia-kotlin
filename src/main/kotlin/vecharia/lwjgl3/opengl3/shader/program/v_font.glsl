#version 330

layout (location = 0) in vec2 position;
layout (location = 1) in vec2 texture_coordinates;

out vec2 texture_coords;

uniform vec2 translation;

void main() {
    gl_Position = vec4(position + translation * vec2(2.0, -2.0), 0.0, 1.0);
    texture_coords = texture_coordinates;
}
