[color]
color=red
[camera]
exposure = 170
width = 320
height = 240
[network_communication]
panda_ip=10.29.14.3
radio_ip = 10.29.14.1
crio_ip = 10.29.14.2
crio_tcp_loc_coords_port = 2914
laptop_ip = 10.29.14.9
laptop_tcp_port = 1180
send_over_network = True
[autonomous]
hue_upper = 21
hue_lower = 14
saturation_upper = 118
saturation_lower = 105
value_upper = 196
value_lower = 185
yellow_pixel_thresh = 70
delay_every_cycle=0.1
[tracking]
hue_upper = 5
hue_lower = 0
saturation_upper = 255
saturation_lower = 105
value_upper = 200
value_lower = 60
upper_bump_detect=130
lower_bump_detect=200
min_contour_area = 2500
area_difference_to_area_for_circle_detect = 15
min_max_radius_for_circle_detect = 1.5
smallest_ball_area_to_return=600
smallest_bumper_area_to_return=1500
[shooting]
hue_upper = 29
hue_lower = 27
saturation_upper = 255
saturation_lower = 0
value_upper = 255
value_lower = 0
lower_avg=40
upper_avg=100
min_pixel_weight=2000
[mathstuff]
[debug]
log_fps=True
crio_on_localhost=False
log_level=5
swap_cameras=True
