from model.geometry.bounding_box import BoundingBox
from model.geometry.point import Point
from model.pivot import Pivot
from model.placement import Placement


class PlacedLink:
    def __init__(self, link, start_pivot:Pivot):
        self.link = link
        self.start_pivot = start_pivot
        self.end_pivot = Pivot(Placement(self.get_end_position(), self.start_pivot.current_orientation))
    
    def get_start(self):
        return self.start_pivot.placement.point
    def get_end(self):
        return self.end_pivot.placement.point
        
    def get_end_position(self):
        # Calculate the end position based on link length and placement vector
        start_point = self.start_pivot.placement.point
        direction_vector = self.start_pivot.current_orientation
        return start_point.offset_distance(direction_vector, self.link.length - 1)
   
    def is_colliding(self, other) -> bool:
        # Simple collision detection based on bounding boxes
        my_points = self.get_points()
        other_points = other.get_points()
        for p in my_points:
            for op in other_points:
                if p.x == op.x and p.y == op.y and p.z == op.z:
                    print(f"Links Collide at Point: {p} : {self} and {other}")
                    return True
                
        print("Links Don't Collide: {self} and {other}")
        return False
        
     
    def get_bounding_box(self) -> BoundingBox:
        # Calculate a simple bounding box based on start and end positions
        start = self.start_pivot.placement.point
        end = self.get_end_position()
        min_x = min(start.x, end.x)
        max_x = max(start.x, end.x)
        min_y = min(start.y, end.y)
        max_y = max(start.y, end.y)
        min_z = min(start.z, end.z)
        max_z = max(start.z, end.z)
        return BoundingBox(Point(min_x, min_y, min_z), Point(max_x, max_y, max_z))
    
    def rotate(self) -> bool:
        if (self.start_pivot.rotate()):
            self.end_pivot = Pivot(Placement(self.get_end_position(), self.start_pivot.current_orientation))
            return True
        
        return False
    
    def get_points(self):
        points = []
        start = self.start_pivot.placement.point
        direction_vector = self.start_pivot.current_orientation
        for i in range(self.link.length):
            point = start.offset_distance(direction_vector, i)
            points.append(point)
        return points