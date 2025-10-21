class BoundingBox:
    def __init__(self, Point_min, Point_max):
        self.min_x = Point_min.x
        self.min_y = Point_min.y
        self.min_z = Point_min.z
        self.max_x = Point_max.x
        self.max_y = Point_max.y
        self.max_z = Point_max.z    
    
    def is_inside(self, x, y, z):
        return (self.min_x <= x <= self.max_x and
                self.min_y <= y <= self.max_y and
                self.min_z <= z <= self.max_z)
    
    def add(self, other_bounding_box):
        self.min_x = min(self.min_x, other_bounding_box.min_x)
        self.min_y = min(self.min_y, other_bounding_box.min_y)
        self.min_z = min(self.min_z, other_bounding_box.min_z)
        self.max_x = max(self.max_x, other_bounding_box.max_x)
        self.max_y = max(self.max_y, other_bounding_box.max_y)
        self.max_z = max(self.max_z, other_bounding_box.max_z)
        
    def get_dimensions(self):
        return (self.max_x - self.min_x,
                self.max_y - self.min_y,
                self.max_z - self.min_z)
    
    def get_max_dimension(self):
        return max(self.get_dimensions())
    
    def get_min_dimension(self):
        return min(self.get_dimensions())
    
    def print(self):
        print(f"BoundingBox(min=({self.min_x}, {self.min_y}, {self.min_z}), max=({self.max_x}, {self.max_y}, {self.max_z}))")
    
    def intersects(self, other) -> bool:
        # do following boxes intersect?
        # (0,0,0) to (1,0,0) and (.5,-1.0) to (.5,1.0)?
        return not (self.max_x <= other.min_x or self.min_x >= other.max_x or
                    self.max_y <= other.min_y or self.min_y >= other.max_y or
                    self.max_z <= other.min_z or self.min_z >= other.max_z)
