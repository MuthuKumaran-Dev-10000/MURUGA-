import tkinter as tk
from tkinter import Canvas, Button, messagebox
from PIL import Image, ImageDraw, ImageOps
import numpy as np
import tensorflow as tf
from tensorflow.keras.models import Sequential, load_model
from tensorflow.keras.layers import Conv2D, MaxPooling2D, Flatten, Dense, Dropout
from tensorflow.keras.datasets import mnist
from tensorflow.keras.utils import to_categorical

# Load MNIST dataset
(x_train, y_train), (x_test, y_test) = mnist.load_data()

# Normalize and reshape data
x_train = x_train.reshape((-1, 28, 28, 1)).astype('float32') / 255.0
x_test = x_test.reshape((-1, 28, 28, 1)).astype('float32') / 255.0

# Convert labels to categorical
y_train = to_categorical(y_train)
y_test = to_categorical(y_test)

# Build CNN model
model = Sequential([
    Conv2D(32, (3, 3), activation='relu', input_shape=(28, 28, 1)),
    MaxPooling2D((2, 2)),
    Conv2D(64, (3, 3), activation='relu'),
    MaxPooling2D((2, 2)),
    Flatten(),
    Dense(128, activation='relu'),
    Dropout(0.5),
    Dense(10, activation='softmax')
])

# Compile model
model.compile(optimizer='adam', loss='categorical_crossentropy', metrics=['accuracy'])

# Train model
model.fit(x_train, y_train, epochs=10, batch_size=128, validation_data=(x_test, y_test))

# Evaluate model
test_loss, test_acc = model.evaluate(x_test, y_test)
print(f"Test accuracy: {test_acc}")

# Save model
model.save('mnist_digit_recognition.h5')

# Function to predict digit from drawn image
def predict_digit():
    global img
    # Resize image to 28x28 pixels
    img_resized = img.resize((28, 28))
    # Convert image to grayscale
    img_gray = img_resized.convert('L')
    # Invert pixel values (MNIST dataset uses white background)
    img_inverted = ImageOps.invert(img_gray)
    # Normalize pixel values
    img_normalized = np.array(img_inverted).astype('float32') / 255.0
    # Reshape to match model input shape (add batch dimension)
    img_input = np.expand_dims(img_normalized, axis=0)
    # Predict digit using the model
    prediction = np.argmax(model.predict(img_input), axis=-1)[0]
    # Display prediction
    messagebox.showinfo("Prediction", f"The digit is: {prediction}")

# Function to track mouse movements and draw on canvas
def paint(event):
    x1, y1 = (event.x - 10), (event.y - 10)
    x2, y2 = (event.x + 10), (event.y + 10)
    canvas.create_oval(x1, y1, x2, y2, fill="black", width=5)
    draw.line([x1, y1, x2, y2], fill="white", width=10)

# Function to clear canvas
def clear_canvas():
    global canvas, img
    canvas.delete("all")
    img = Image.new("RGB", (300, 300), (0, 0, 0))
    draw = ImageDraw.Draw(img)

# Create main window
root = tk.Tk()
root.title("Digit Recognition")

# Create canvas for drawing
canvas = Canvas(root, width=300, height=300, bg='white')
canvas.pack()

# Initialize drawing parameters
img = Image.new("RGB", (300, 300), (0, 0, 0))
draw = ImageDraw.Draw(img)

# Bind mouse movements to canvas
canvas.bind("<B1-Motion>", paint)

# Create buttons
button_predict = Button(root, text="Predict", command=predict_digit)
button_predict.pack(side=tk.LEFT, padx=10)
button_clear = Button(root, text="Clear", command=clear_canvas)
button_clear.pack(side=tk.RIGHT, padx=10)

# Run main loop
root.mainloop()
