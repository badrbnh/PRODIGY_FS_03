import {postData, fetchData, updateData, deleteData} from "./dataOperations";

export function addArticle(data, file) {
    return postData("/articles/add", data, file);
}

export function deleteArticle(id) {
    return deleteData(`/articles/delete/${id}`);
}

export function updateArticle(id, data, file) {
    return updateData(`/articles/update/${id}`, data, file);
}

export function getArticles() {
    return fetchData("/articles/all");
}

export function getArticleImageUrl(relativePath) {
    return `${process.env.REACT_APP_BASE_URL}/${relativePath}`;
}