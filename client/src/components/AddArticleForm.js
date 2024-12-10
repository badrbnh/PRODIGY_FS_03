import React, { useState } from "react";
import { addArticle } from "../app/features/sevices/addArticle";

const AddArticleForm = () => {
    const [formData, setFormData] = useState({
        name: "",
        description: "",
        price: "",
        quantity: "",
    });
    const [file, setFile] = useState(null);
    const [message, setMessage] = useState("");

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleFileChange = (e) => {
        setFile(e.target.files[0]);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            await addArticle(formData, file);
            setMessage("Article added successfully!");
            setFormData({ name: "", description: "", price: "", quantity: "" });
            setFile(null);
        } catch (error) {
            setMessage("Error adding article. Please try again.");
            console.error(error);
        }
    };

    return (
        <div>
            <h2>Add New Article</h2>
            <form onSubmit={handleSubmit} encType="multipart/form-data">
                <div>
                    <label htmlFor="name">Name:</label>
                    <input
                        type="text"
                        id="name"
                        name="name"
                        value={formData.name}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div>
                    <label htmlFor="description">Description:</label>
                    <textarea
                        id="description"
                        name="description"
                        value={formData.description}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div>
                    <label htmlFor="price">Price:</label>
                    <input
                        type="number"
                        id="price"
                        name="price"
                        value={formData.price}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div>
                    <label htmlFor="quantity">Quantity:</label>
                    <input
                        type="number"
                        id="quantity"
                        name="quantity"
                        value={formData.quantity}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div>
                    <label htmlFor="file">Image:</label>
                    <input
                        type="file"
                        id="file"
                        name="file"
                        onChange={handleFileChange}
                        accept="image/*"
                    />
                </div>
                <button type="submit">Add Article</button>
            </form>
            {message && <p>{message}</p>}
        </div>
    );
};

export default AddArticleForm;
