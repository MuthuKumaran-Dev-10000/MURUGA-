from PIL import Image, ImageDraw
import numpy as np
import tkinter as tk
from tkinter import filedialog
import os

def invert_colors(image):
    # Invert colors of the image
    inverted_image = Image.eval(image, lambda x: 255 - x)

    return inverted_image

def process_image(input_file):
    # Open the input image
    image = Image.open(input_file)

    # Invert colors of the image
    inverted_image = invert_colors(image)

    # Save the output image as output.png
    output_path = os.path.join("C:\\Users\\muthu\\OneDrive\\Desktop\\TCE\\Output", "output.png")
    inverted_image.save(output_path)
    return output_path

# Process the image directly with the provided path
input_file = "C:\\Users\\muthu\\OneDrive\\Pictures\\Agile Sprint.png"
output_file = process_image(input_file)

print(f"Inverted image saved at: {output_file}")
