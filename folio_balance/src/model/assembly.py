from model.geometry.bounding_box import BoundingBox
from .placed_link import PlacedLink

class Assembly:
    def __init__(self):
        self.placed_links = []
        
    def place_link(self, placed_link):
        self.placed_links.append(placed_link)
        
    def get_bounding_box(self) -> 'BoundingBox':
        if not self.placed_links:
            return None
        overall_bounding_box = self.placed_links[0].get_bounding_box()
        for pl in self.placed_links[1:]:
            overall_bounding_box.add(pl.get_bounding_box())
        return overall_bounding_box
    
    def pop_last_link(self):
        if self.placed_links:
            return self.placed_links.pop()
        return None
    
    def size(self):
        return len(self.placed_links)
    
    def is_empty(self):
        return len(self.placed_links) == 0
    
    def get_last_placed_link(self):
        if self.placed_links:
            return self.placed_links[-1]
        return None
    
    def get_placed_links(self):
        return self.placed_links
    
    def invert(self):
        self.placed_links.reverse()
        
    def print_assembly(self):
        for i, pl in enumerate(self.placed_links):
            print(f"Link {i}: Length={pl.link.length}, Start={pl.start_pivot.placement.point.x, pl.start_pivot.placement.point.y, pl.start_pivot.placement.point.z}, Orientation={pl.start_pivot.current_orientation.x, pl.start_pivot.current_orientation.y, pl.start_pivot.current_orientation.z}")
            
        # print bounding box
        bbox = self.get_bounding_box()
        if bbox:
            print("Overall Bounding Box:")
            bbox.print()
            
    def is_valid(self) -> bool:
        # Check bounding box has size greater than 3 in any dimension
        bbox = self.get_bounding_box()
        if bbox is None or bbox.get_max_dimension() > 2:
            return False
        
        # Check for collisions between placed links
        for i in range(len(self.placed_links)):
            for j in range(i + 2, len(self.placed_links)):
                if self.placed_links[i].is_colliding(self.placed_links[j]):
                    return False
        
        return True
    