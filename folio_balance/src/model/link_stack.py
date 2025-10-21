from model.link import Link


class LinkStack:
    def __init__(self, links = []):
        self.links = links
        
    def total_length(self):
        return sum(link.length for link in self.links)
   
    def size(self):
        return len(self.links)
    
    def pop_link(self) -> Link | None:
        if self.links:
            return self.links.pop()
        return None
    
    def push_link(self, link):
        self.links.append(link)
       
    def is_empty(self):
        return len(self.links) == 0
     
    def invert(self):
        self.links.reverse() 