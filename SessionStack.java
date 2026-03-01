public class SessionStack {
    private class Node {
        String username;
        Node next;
        Node(String username) { this.username = username; }
    }
    
    private Node top;

    public void login(String user) {
        Node newNode = new Node(user);
        newNode.next = top;
        top = newNode;
    }

    public void logout() {
        if (top != null) top = top.next;
    }

    // Method ini menghilangkan warning 'variable never read'
    public String getCurrentUser() {
        return (top != null) ? top.username : null;
    }
    
    public boolean isLoggedIn() { return top != null; }
}
