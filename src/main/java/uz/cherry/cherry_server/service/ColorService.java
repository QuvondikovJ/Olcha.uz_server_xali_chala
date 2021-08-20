package uz.cherry.cherry_server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.cherry.cherry_server.entity.Color;
import uz.cherry.cherry_server.payload.Result;
import uz.cherry.cherry_server.repository.ColorRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ColorService {

    @Autowired
    ColorRepository colorRepository;

    public Result add(Color color) {
        boolean existsByNameAndActive = colorRepository.existsByNameAndActive(color.getName(), true);
        if (existsByNameAndActive) return new Result("This color already added!", false);
        colorRepository.save(color);
        return new Result("New color successfully saved.", true);
    }

    public Result get() {
        List<Color> colors = colorRepository.findAllByActive(true);
        if (colors.size() == 0) return new Result("No color has been added yet!", false);
        return new Result(colors, true);
    }

    public Result getById(Integer id) {
        Optional<Color> optionalColor = colorRepository.findByIdAndActive(id, true);
        return optionalColor.map(color -> new Result(color, true)).orElseGet(() -> new Result("Such color id not exist!", false));
    }

    public Result edit(Integer colorId, Color newColor) {
        Optional<Color> optionalColor = colorRepository.findByIdAndActive(colorId, true);
        if (!optionalColor.isPresent()) return new Result("Such color id not exist!", false);
        boolean existsByNameAndActive = colorRepository.existsByNameAndActive(newColor.getName(), true);
        if (existsByNameAndActive) return new Result("This color already added.", false);
        Color color = optionalColor.get();
        color.setName(newColor.getName());
        colorRepository.save(color);
        return new Result("Given color successfully edited.", true);
    }

    public Result delete(Integer id) {
        Optional<Color> optionalColor = colorRepository.findByIdAndActive(id, true);
        if (!optionalColor.isPresent()) return new Result("Such color id not exist!", false);
        Color color = optionalColor.get();
        color.setActive(false);
        colorRepository.save(color);
        return new Result("Given color successfully deleted.", true);
    }


}
