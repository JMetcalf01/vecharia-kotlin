#version 330

in vec2 texture_coords;

out vec4 pixel_color;

uniform vec4 color;
uniform sampler2D font_atlas;

const float width = 0.5; //todo these are font specific and should be loaded in uniforms
const float edge = 0.2;

// Nothing
const float border_width = 0.0f;
const float border_edge = 0.4f;

// Border
//const float border_width = 0.7;
//const float border_edge = 0.1;

// Glow
//const float border_width = 0.4;
//const float border_edge = 0.5;

const vec3 outline_color = vec3(0.0, 0.0, 1.0);

void main() {
    float distance = 1.0 - texture(font_atlas, texture_coords).a;
    float alpha = 1.0 - smoothstep(width, width + edge, distance);

    pixel_color = vec4(color.xyz, alpha);
//    float outline_alpha = 1.0 - smoothstep(border_width, border_width + border_edge, distance);

//    float total_alpha = alpha + (1.0 - alpha) * outline_alpha;
//    vec3 total_color = mix(color.xyz, outline_color, alpha / total_alpha);

//    pixel_color = vec4(total_color, total_alpha);
}
